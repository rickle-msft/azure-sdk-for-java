// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.


package com.azure.storage.blob

import com.azure.core.http.*
import com.azure.core.http.policy.HttpLogDetailLevel
import com.azure.core.http.policy.HttpPipelinePolicy
import com.azure.core.http.rest.Response
import com.azure.storage.blob.BlobProperties
import com.azure.storage.blob.models.*
import com.azure.storage.common.Constants
import com.azure.storage.common.policy.RequestRetryOptions
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Unroll

import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest

class BlockBlobAPITest extends APISpec {
    BlockBlobClient bc
    BlockBlobAsyncClient bac
    BlockBlobClient bec // encrypted client
    BlockBlobAsyncClient beac // encrypted async client

    String keyId
    IKey symmetricKey
    BlobEncryptionPolicy blobEncryptionPolicy

    def setup() {
        bc = cc.getBlockBlobClient(generateBlobName())
        bc.upload(defaultInputStream.get(), defaultDataSize)
        bac = ccAsync.getBlockBlobAsyncClient(generateBlobName())
        bac.upload(defaultFlux, defaultDataSize)

        keyId = "keyId"
        KeyGenerator keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)
        SecretKey secretKey = keyGen.generateKey()
        symmetricKey = new SymmetricKey(keyId, secretKey.getEncoded())

        blobEncryptionPolicy = new BlobEncryptionPolicy(symmetricKey, null, false)
        bec = cc.getBlockBlobClient(generateBlobName(), null, blobEncryptionPolicy)
        beac = ccAsync.getBlockBlobAsyncClient(generateBlobName(), null, blobEncryptionPolicy)
    }

    def "Stage block"() {
        setup:
        def response = bc.stageBlockWithResponse(getBlockID(), defaultInputStream.get(), defaultDataSize, null, null, null)
        HttpHeaders headers = response.headers()

        expect:
        response.statusCode() == 201
        headers.value("x-ms-content-crc64") != null
        headers.value("x-ms-request-id") != null
        headers.value("x-ms-version") != null
        headers.value("Date") != null
        Boolean.parseBoolean(headers.value("x-ms-request-server-encrypted"))
    }

    def "Stage block min"() {
        when:
        bc.stageBlock(getBlockID(), defaultInputStream.get(), defaultDataSize) == 201

        then:
        bc.listBlocks(BlockListType.ALL).uncommittedBlocks().size() == 1
    }

    @Unroll
    def "Stage block illegal arguments"() {
        when:
        String blockID = (getBlockId) ? getBlockID() : null
        bc.stageBlock(blockID, data == null ? null : data.get(), dataSize)

        then:
        def e = thrown(Exception)
        exceptionType.isInstance(e)

        where:
        getBlockId | data               | dataSize            | exceptionType
        false      | defaultInputStream | defaultDataSize     | StorageException
        true       | null               | defaultDataSize     | NullPointerException
        true       | defaultInputStream | defaultDataSize + 1 | IndexOutOfBoundsException
        // TODO (alzimmer): This doesn't throw an error as the stream is larger than the stated size
        //true         | defaultInputStream   | defaultDataSize - 1 | IllegalArgumentException
    }

    def "Stage block empty body"() {
        when:
        bc.stageBlock(getBlockID(), new ByteArrayInputStream(new byte[0]), 0)

        then:
        thrown(StorageException)
    }

    def "Stage block null body"() {
        when:
        bc.stageBlock(getBlockID(), null, 0)

        then:
        thrown(StorageException)
    }

    def "Stage block lease"() {
        setup:
        String leaseID = setupBlobLeaseCondition(bc, receivedLeaseID)

        expect:
        bc.stageBlockWithResponse(getBlockID(), defaultInputStream.get(), defaultDataSize, new LeaseAccessConditions().leaseId(leaseID),
            null, null).statusCode() == 201
    }

    def "Stage block lease fail"() {
        setup:
        setupBlobLeaseCondition(bc, receivedLeaseID)

        when:
        bc.stageBlockWithResponse(getBlockID(), defaultInputStream.get(), defaultDataSize, new LeaseAccessConditions()
            .leaseId(garbageLeaseID), null, null)

        then:
        def e = thrown(StorageException)
        e.errorCode() == StorageErrorCode.LEASE_ID_MISMATCH_WITH_BLOB_OPERATION
    }

    def "Stage block error"() {
        setup:
        bc = cc.getBlockBlobClient(generateBlobName())

        when:
        bc.stageBlock("id", defaultInputStream.get(), defaultDataSize)

        then:
        thrown(StorageException)
    }

    def "Stage block from url"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def bu2 = cc.getBlockBlobClient(generateBlobName())
        def blockID = getBlockID()

        when:
        def headers = bu2.stageBlockFromURLWithResponse(blockID, bc.getBlobUrl(), null, null, null, null, null, null).headers()

        then:
        headers.value("x-ms-request-id") != null
        headers.value("x-ms-version") != null
        headers.value("x-ms-content-crc64") != null
        headers.value("x-ms-request-server-encrypted") != null

        def response = bu2.listBlocks(BlockListType.ALL)
        response.uncommittedBlocks().size() == 1
        response.committedBlocks().size() == 0
        response.uncommittedBlocks().first().name() == blockID

        when:
        bu2.commitBlockList(Arrays.asList(blockID))
        def outputStream = new ByteArrayOutputStream()
        bu2.download(outputStream)

        then:
        ByteBuffer.wrap(outputStream.toByteArray()) == defaultData
    }

    def "Stage block from url min"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def bu2 = cc.getBlockBlobClient(generateBlobName())
        def blockID = getBlockID()

        expect:
        bu2.stageBlockFromURLWithResponse(blockID, bc.getBlobUrl(), null, null, null, null, null, null).statusCode() == 201
    }

    @Unroll
    def "Stage block from URL IA"() {
        when:
        String blockID = (getBlockId) ? getBlockID() : null
        bc.stageBlockFromURL(blockID, sourceURL, null)

        then:
        thrown(StorageException)

        where:
        getBlockId | sourceURL
        false      | new URL("http://www.example.com")
        true       | null
    }

    def "Stage block from URL range"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def destURL = cc.getBlockBlobClient(generateBlobName())

        when:
        destURL.stageBlockFromURL(getBlockID(), bc.getBlobUrl(), new BlobRange(2, 3))
        Iterator<BlockItem> uncommittedBlock = destURL.listBlocks(BlockListType.UNCOMMITTED).iterator()

        then:
        uncommittedBlock.hasNext()
        uncommittedBlock.hasNext()
        uncommittedBlock.hasNext()
    }

    def "Stage block from URL MD5"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def destURL = cc.getBlockBlobClient(generateBlobName())

        when:
        destURL.stageBlockFromURLWithResponse(getBlockID(), bc.getBlobUrl(), null,
            MessageDigest.getInstance("MD5").digest(defaultData.array()), null, null, null, null)

        then:
        notThrown(StorageException)
    }

    def "Stage block from URL MD5 fail"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def destURL = cc.getBlockBlobClient(generateBlobName())

        when:
        destURL.stageBlockFromURLWithResponse(getBlockID(), bc.getBlobUrl(), null, "garbage".getBytes(),
            null, null, null, null)

        then:
        thrown(StorageException)
    }

    def "Stage block from URL lease"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def lease = new LeaseAccessConditions().leaseId(setupBlobLeaseCondition(bc, receivedLeaseID))

        when:
        bc.stageBlockFromURLWithResponse(getBlockID(), bc.getBlobUrl(), null, null, lease, null, null, null)

        then:
        notThrown(StorageException)
    }

    def "Stage block from URL lease fail"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def lease = new LeaseAccessConditions().leaseId("garbage")

        when:
        bc.stageBlockFromURLWithResponse(getBlockID(), bc.getBlobUrl(), null, null, lease, null, null, null)

        then:
        thrown(StorageException)
    }

    def "Stage block from URL error"() {
        setup:
        bc = primaryBlobServiceClient.getContainerClient(generateContainerName()).getBlockBlobClient(generateBlobName())

        when:
        bc.stageBlockFromURL(getBlockID(), bc.getBlobUrl(), null)

        then:
        thrown(StorageException)
    }

    @Unroll
    def "Stage block from URL source AC"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def blockID = getBlockID()

        def sourceURL = cc.getBlockBlobClient(generateBlobName())
        sourceURL.upload(defaultInputStream.get(), defaultDataSize)

        sourceIfMatch = setupBlobMatchCondition(sourceURL, sourceIfMatch)
        def smac = new SourceModifiedAccessConditions()
            .sourceIfModifiedSince(sourceIfModifiedSince)
            .sourceIfUnmodifiedSince(sourceIfUnmodifiedSince)
            .sourceIfMatch(sourceIfMatch)
            .sourceIfNoneMatch(sourceIfNoneMatch)

        expect:
        bc.stageBlockFromURLWithResponse(blockID, sourceURL.getBlobUrl(), null, null, null, smac, null, null).statusCode() == 201

        where:
        sourceIfModifiedSince | sourceIfUnmodifiedSince | sourceIfMatch | sourceIfNoneMatch
        null                  | null                    | null          | null
        oldDate               | null                    | null          | null
        null                  | newDate                 | null          | null
        null                  | null                    | receivedEtag  | null
        null                  | null                    | null          | garbageEtag
    }

    @Unroll
    def "Stage block from URL source AC fail"() {
        setup:
        cc.setAccessPolicy(PublicAccessType.CONTAINER, null)
        def blockID = getBlockID()

        def sourceURL = cc.getBlockBlobClient(generateBlobName())
        sourceURL.upload(defaultInputStream.get(), defaultDataSize)

        def smac = new SourceModifiedAccessConditions()
            .sourceIfModifiedSince(sourceIfModifiedSince)
            .sourceIfUnmodifiedSince(sourceIfUnmodifiedSince)
            .sourceIfMatch(sourceIfMatch)
            .sourceIfNoneMatch(setupBlobMatchCondition(sourceURL, sourceIfNoneMatch))

        when:
        bc.stageBlockFromURLWithResponse(blockID, sourceURL.getBlobUrl(), null, null, null, smac, null, null).statusCode() == 201

        then:
        thrown(StorageException)

        where:
        sourceIfModifiedSince | sourceIfUnmodifiedSince | sourceIfMatch | sourceIfNoneMatch
        newDate               | null                    | null          | null
        null                  | oldDate                 | null          | null
        null                  | null                    | garbageEtag   | null
        null                  | null                    | null          | receivedEtag
    }

    def "Commit block list"() {
        setup:
        String blockID = getBlockID()
        bc.stageBlock(blockID, defaultInputStream.get(), defaultDataSize)
        ArrayList<String> ids = new ArrayList<>()
        ids.add(blockID)

        when:
        def response = bc.commitBlockListWithResponse(ids, null, null, null, null, null)
        def headers = response.headers()

        then:
        response.statusCode() == 201
        validateBasicHeaders(headers)
        headers.value("x-ms-content-crc64")
        Boolean.parseBoolean(headers.value("x-ms-request-server-encrypted"))
    }

    def "Commit block list min"() {
        setup:
        String blockID = getBlockID()
        bc.stageBlock(blockID, defaultInputStream.get(), defaultDataSize)
        ArrayList<String> ids = new ArrayList<>()
        ids.add(blockID)

        expect:
        bc.commitBlockList(ids) != null
    }

    def "Commit block list null"() {
        expect:
        bc.commitBlockListWithResponse(null, null, null, null, null, null).statusCode() == 201
    }

    @Unroll
    def "Commit block list headers"() {
        setup:
        String blockID = getBlockID()
        bc.stageBlock(blockID, defaultInputStream.get(), defaultDataSize)
        ArrayList<String> ids = new ArrayList<>()
        ids.add(blockID)
        BlobHTTPHeaders headers = new BlobHTTPHeaders().blobCacheControl(cacheControl)
            .blobContentDisposition(contentDisposition)
            .blobContentEncoding(contentEncoding)
            .blobContentLanguage(contentLanguage)
            .blobContentMD5(contentMD5)
            .blobContentType(contentType)

        when:
        bc.commitBlockListWithResponse(ids, headers, null, null, null, null)
        def response = bc.getPropertiesWithResponse(null, null, null)

        // If the value isn't set the service will automatically set it
        contentType = (contentType == null) ? "application/octet-stream" : contentType

        then:
        validateBlobProperties(response, cacheControl, contentDisposition, contentEncoding, contentLanguage, contentMD5, contentType)

        where:
        cacheControl | contentDisposition | contentEncoding | contentLanguage | contentMD5                                                   | contentType
        null         | null               | null            | null            | null                                                         | null
        "control"    | "disposition"      | "encoding"      | "language"      | MessageDigest.getInstance("MD5").digest(defaultData.array()) | "type"
    }

    @Unroll
    def "Commit block list metadata"() {
        setup:
        Metadata metadata = new Metadata()
        if (key1 != null) {
            metadata.put(key1, value1)
        }
        if (key2 != null) {
            metadata.put(key2, value2)
        }

        when:
        bc.commitBlockListWithResponse(null, null, metadata, null, null, null)
        def response = bc.getPropertiesWithResponse(null, null, null)

        then:
        response.statusCode() == 200
        response.value().metadata() == metadata

        where:
        key1  | value1 | key2   | value2
        null  | null   | null   | null
        "foo" | "bar"  | "fizz" | "buzz"
    }

    @Unroll
    def "Commit block list AC"() {
        setup:
        match = setupBlobMatchCondition(bc, match)
        leaseID = setupBlobLeaseCondition(bc, leaseID)
        BlobAccessConditions bac = new BlobAccessConditions()
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))
            .modifiedAccessConditions(new ModifiedAccessConditions()
            .ifModifiedSince(modified)
            .ifUnmodifiedSince(unmodified)
            .ifMatch(match)
            .ifNoneMatch(noneMatch))


        expect:
        bc.commitBlockListWithResponse(null, null, null, bac, null, null).statusCode() == 201

        where:
        modified | unmodified | match        | noneMatch   | leaseID
        null     | null       | null         | null        | null
        oldDate  | null       | null         | null        | null
        null     | newDate    | null         | null        | null
        null     | null       | receivedEtag | null        | null
        null     | null       | null         | garbageEtag | null
        null     | null       | null         | null        | receivedLeaseID
    }

    @Unroll
    def "Commit block list AC fail"() {
        setup:
        noneMatch = setupBlobMatchCondition(bc, noneMatch)
        setupBlobLeaseCondition(bc, leaseID)
        BlobAccessConditions bac = new BlobAccessConditions()
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))
            .modifiedAccessConditions(new ModifiedAccessConditions()
            .ifModifiedSince(modified)
            .ifUnmodifiedSince(unmodified)
            .ifMatch(match)
            .ifNoneMatch(noneMatch))

        when:
        bc.commitBlockListWithResponse(null, null, null, bac, null, null)
        then:
        def e = thrown(StorageException)
        e.errorCode() == StorageErrorCode.CONDITION_NOT_MET ||
            e.errorCode() == StorageErrorCode.LEASE_ID_MISMATCH_WITH_BLOB_OPERATION

        where:
        modified | unmodified | match       | noneMatch    | leaseID
        newDate  | null       | null        | null         | null
        null     | oldDate    | null        | null         | null
        null     | null       | garbageEtag | null         | null
        null     | null       | null        | receivedEtag | null
        null     | null       | null        | null         | garbageLeaseID
    }

    def "Commit block list error"() {
        setup:
        bc = cc.getBlockBlobClient(generateBlobName())

        when:
        bc.commitBlockListWithResponse(new ArrayList<String>(), null, null,
            new BlobAccessConditions().leaseAccessConditions(new LeaseAccessConditions().leaseId("garbage")), null, null)

        then:
        thrown(StorageException)
    }

    def "Get block list"() {
        setup:
        def committedBlocks = [getBlockID(), getBlockID()]
        bc.stageBlock(committedBlocks.get(0), defaultInputStream.get(), defaultDataSize)
        bc.stageBlock(committedBlocks.get(1), defaultInputStream.get(), defaultDataSize)
        bc.commitBlockList(committedBlocks)

        def uncommittedBlocks = [getBlockID(), getBlockID()]
        bc.stageBlock(uncommittedBlocks.get(0), defaultInputStream.get(), defaultDataSize)
        bc.stageBlock(uncommittedBlocks.get(1), defaultInputStream.get(), defaultDataSize)
        uncommittedBlocks.sort(true)

        when:
        def blockList = bc.listBlocks(BlockListType.ALL)

        then:
        blockList.committedBlocks().collect { it.name() } as Set == committedBlocks as Set
        blockList.uncommittedBlocks().collect { it.name() } as Set == uncommittedBlocks as Set

        (blockList.committedBlocks() + blockList.uncommittedBlocks())
            .each { assert it.size() == defaultDataSize }
    }

    def "Get block list min"() {
        when:
        bc.listBlocks(BlockListType.ALL)

        then:
        notThrown(StorageErrorException)
    }

    @Unroll
    def "Get block list type"() {
        setup:
        def blockID = getBlockID()
        bc.stageBlock(blockID, defaultInputStream.get(), defaultDataSize)
        bc.commitBlockList([blockID])
        bc.stageBlock(getBlockID(), defaultInputStream.get(), defaultDataSize)

        when:
        def response = bc.listBlocks(type)

        then:
        response.committedBlocks().size() == committedCount
        response.uncommittedBlocks().size() == uncommittedCount

        where:
        type                      | committedCount | uncommittedCount
        BlockListType.ALL         | 1              | 1
        BlockListType.COMMITTED   | 1              | 0
        BlockListType.UNCOMMITTED | 0              | 1
    }

    def "Get block list type null"() {
        when:
        bc.listBlocks(null).iterator().hasNext()

        then:
        notThrown(IllegalArgumentException)
    }

    def "Get block list lease"() {
        setup:
        String leaseID = setupBlobLeaseCondition(bc, receivedLeaseID)

        when:
        bc.listBlocksWithResponse(BlockListType.ALL, new LeaseAccessConditions().leaseId(leaseID), null)

        then:
        notThrown(StorageException)
    }

    def "Get block list lease fail"() {
        setup:
        setupBlobLeaseCondition(bc, garbageLeaseID)

        when:
        bc.listBlocksWithResponse(BlockListType.ALL, new LeaseAccessConditions().leaseId(garbageLeaseID), null)

        then:
        def e = thrown(StorageException)
        e.errorCode() == StorageErrorCode.LEASE_ID_MISMATCH_WITH_BLOB_OPERATION
    }

    def "Get block list error"() {
        setup:
        bc = cc.getBlockBlobClient(generateBlobName())

        when:
        bc.listBlocks(BlockListType.ALL).iterator().hasNext()

        then:
        thrown(StorageException)
    }

    def "Upload"() {
        when:
        def response = bc.uploadWithResponse(defaultInputStream.get(), defaultDataSize, null, null, null, null, null)

        then:
        response.statusCode() == 201
        def outStream = new ByteArrayOutputStream()
        bc.download(outStream)
        outStream.toByteArray() == defaultText.getBytes(StandardCharsets.UTF_8)
        validateBasicHeaders(response.headers())
        response.headers().value("Content-MD5") != null
        Boolean.parseBoolean(response.headers().value("x-ms-request-server-encrypted"))
    }

    def "Upload min"() {
        when:
        bc.upload(defaultInputStream.get(), defaultDataSize)

        then:
        def outStream = new ByteArrayOutputStream()
        bc.download(outStream)
        outStream.toByteArray() == defaultText.getBytes(StandardCharsets.UTF_8)
    }

    @Unroll
    def "Upload illegal argument"() {
        when:
        bc.upload(data, dataSize)

        then:
        def e = thrown(Exception)
        exceptionType.isInstance(e)

        where:
        data                     | dataSize            | exceptionType
        null                     | defaultDataSize     | NullPointerException
        defaultInputStream.get() | defaultDataSize + 1 | IndexOutOfBoundsException
        // This doesn't error as it isn't reading the entire stream which is valid in the new client
        // defaultInputStream.get() | defaultDataSize - 1 | StorageErrorException
    }

    def "Upload empty body"() {
        expect:
        bc.uploadWithResponse(new ByteArrayInputStream(new byte[0]), 0, null, null, null, null, null).statusCode() == 201
    }

    def "Upload null body"() {
        expect:
        bc.uploadWithResponse(null, 0, null, null, null, null, null).statusCode() == 201
    }

    @Unroll
    def "Upload headers"() {
        setup:
        BlobHTTPHeaders headers = new BlobHTTPHeaders().blobCacheControl(cacheControl)
            .blobContentDisposition(contentDisposition)
            .blobContentEncoding(contentEncoding)
            .blobContentLanguage(contentLanguage)
            .blobContentMD5(contentMD5)
            .blobContentType(contentType)

        when:
        bc.uploadWithResponse(defaultInputStream.get(), defaultDataSize, headers, null, null, null, null)
        def response = bc.getPropertiesWithResponse(null, null, null)

        // If the value isn't set the service will automatically set it
        contentMD5 = (contentMD5 == null) ? MessageDigest.getInstance("MD5").digest(defaultData.array()) : contentMD5
        contentType = (contentType == null) ? "application/octet-stream" : contentType

        then:
        validateBlobProperties(response, cacheControl, contentDisposition, contentEncoding, contentLanguage, contentMD5, contentType)

        where:
        cacheControl | contentDisposition | contentEncoding | contentLanguage | contentMD5                                                   | contentType
        null         | null               | null            | null            | null                                                         | null
        "control"    | "disposition"      | "encoding"      | "language"      | MessageDigest.getInstance("MD5").digest(defaultData.array()) | "type"
    }

    @Unroll
    def "Upload metadata"() {
        setup:
        Metadata metadata = new Metadata()
        if (key1 != null) {
            metadata.put(key1, value1)
        }
        if (key2 != null) {
            metadata.put(key2, value2)
        }

        when:
        bc.uploadWithResponse(defaultInputStream.get(), defaultDataSize, null, metadata, null, null, null)
        def response = bc.getPropertiesWithResponse(null, null, null)

        then:
        response.statusCode() == 200
        response.value().metadata() == metadata

        where:
        key1  | value1 | key2   | value2
        null  | null   | null   | null
        "foo" | "bar"  | "fizz" | "buzz"
    }

    @Unroll
    def "Upload AC"() {
        setup:
        match = setupBlobMatchCondition(bc, match)
        leaseID = setupBlobLeaseCondition(bc, leaseID)
        BlobAccessConditions bac = new BlobAccessConditions()
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))
            .modifiedAccessConditions(new ModifiedAccessConditions()
            .ifModifiedSince(modified)
            .ifUnmodifiedSince(unmodified)
            .ifMatch(match)
            .ifNoneMatch(noneMatch))


        expect:
        bc.uploadWithResponse(defaultInputStream.get(), defaultDataSize, null, null, bac, null, null).statusCode() == 201

        where:
        modified | unmodified | match        | noneMatch   | leaseID
        null     | null       | null         | null        | null
        oldDate  | null       | null         | null        | null
        null     | newDate    | null         | null        | null
        null     | null       | receivedEtag | null        | null
        null     | null       | null         | garbageEtag | null
        null     | null       | null         | null        | receivedLeaseID
    }

    @Unroll
    def "Upload AC fail"() {
        setup:
        noneMatch = setupBlobMatchCondition(bc, noneMatch)
        setupBlobLeaseCondition(bc, leaseID)
        BlobAccessConditions bac = new BlobAccessConditions()
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))
            .modifiedAccessConditions(new ModifiedAccessConditions()
            .ifModifiedSince(modified)
            .ifUnmodifiedSince(unmodified)
            .ifMatch(match)
            .ifNoneMatch(noneMatch))

        when:
        bc.uploadWithResponse(defaultInputStream.get(), defaultDataSize, null, null, bac, null, null)

        then:
        def e = thrown(StorageException)
        e.errorCode() == StorageErrorCode.CONDITION_NOT_MET ||
            e.errorCode() == StorageErrorCode.LEASE_ID_MISMATCH_WITH_BLOB_OPERATION

        where:
        modified | unmodified | match       | noneMatch    | leaseID
        newDate  | null       | null        | null         | null
        null     | oldDate    | null        | null         | null
        null     | null       | garbageEtag | null         | null
        null     | null       | null        | receivedEtag | null
        null     | null       | null        | null         | garbageLeaseID
    }

    def "Upload error"() {
        setup:
        bc = cc.getBlockBlobClient(generateBlobName())

        when:
        bc.uploadWithResponse(defaultInputStream.get(), defaultDataSize, null, null,
            new BlobAccessConditions().leaseAccessConditions(new LeaseAccessConditions().leaseId("id")),
            null, null)

        then:
        thrown(StorageException)
    }

    @Unroll
    def "Async buffered upload"() {
        when:
        def data = getRandomData(dataSize)
        bac.upload(Flux.just(data), bufferSize, numBuffs).block()
        data.position(0)

        then:
        // Due to memory issues, this check only runs on small to medium sized data sets.
        if (dataSize < 100 * 1024 * 1024) {
            assert collectBytesInBuffer(bac.download().block()).block() == data
        }
        bac.listBlocks(BlockListType.ALL).block().committedBlocks().size() == blockCount

        where:
        dataSize          | bufferSize        | numBuffs || blockCount
        350               | 50                | 2        || 7 // Requires cycling through the same buffers multiple times.
        350               | 50                | 5        || 7 // Most buffers may only be used once.
        10 * 1024 * 1024  | 1 * 1024 * 1024   | 2        || 10 // Larger data set.
        10 * 1024 * 1024  | 1 * 1024 * 1024   | 5        || 10 // Larger number of Buffs.
        10 * 1024 * 1024  | 1 * 1024 * 1024   | 10       || 10 // Exactly enough buffer space to hold all the data.
        500 * 1024 * 1024 | 100 * 1024 * 1024 | 2        || 5 // Larger data.
        100 * 1024 * 1024 | 20 * 1024 * 1024  | 4        || 5
        10 * 1024 * 1024  | 3 * 512 * 1024    | 3        || 7 // Data does not squarely fit in buffers.
    }

    def compareListToBuffer(List<ByteBuffer> buffers, ByteBuffer result) {
        result.position(0)
        for (ByteBuffer buffer : buffers) {
            buffer.position(0)
            result.limit(result.position() + buffer.remaining())
            if (buffer != result) {
                return false
            }
            result.position(result.position() + buffer.remaining())
        }
        return result.remaining() == 0
    }

    @Unroll
    def "Buffered upload chunked source"() {
        /*
        This test should validate that the upload should work regardless of what format the passed data is in because
        it will be chunked appropriately.
         */
        setup:
        List<ByteBuffer> dataList = new ArrayList<>()
        dataSizeList.each { size -> dataList.add(getRandomData(size)) }
        bac.upload(Flux.fromIterable(dataList), bufferSize, numBuffers).block()

        expect:
        compareListToBuffer(dataList, collectBytesInBuffer(bac.download().block()).block())
        bac.listBlocks(BlockListType.ALL).block().committedBlocks().size() == blockCount

        where:
        dataSizeList          | bufferSize | numBuffers || blockCount
        [7, 7]                | 10         | 2          || 2 // First item fits entirely in the buffer, next item spans two buffers
        [3, 3, 3, 3, 3, 3, 3] | 10         | 2          || 3 // Multiple items fit non-exactly in one buffer.
        [10, 10]              | 10         | 2          || 2 // Data fits exactly and does not need chunking.
        [50, 51, 49]          | 10         | 2          || 15 // Data needs chunking and does not fit neatly in buffers. Requires waiting for buffers to be released.
        // The case of one large buffer needing to be broken up is tested in the previous test.
    }

    def "Buffered upload illegal arguments null"() {
        when:
        bac.upload(null, 4, 4)

        then:
        thrown(NullPointerException)
    }

    @Unroll
    def "Buffered upload illegal args out of bounds"() {
        when:
        bac.upload(Flux.just(defaultData), bufferSize, numBuffs)

        then:
        thrown(IllegalArgumentException)

        where:
        bufferSize                                     | numBuffs
        0                                              | 5
        BlockBlobAsyncClient.MAX_STAGE_BLOCK_BYTES + 1 | 5
        5                                              | 1
    }

    @Unroll
    def "Buffered upload headers"() {
        when:
        bac.uploadWithResponse(defaultFlux, 10, 2, new BlobHTTPHeaders().blobCacheControl(cacheControl)
            .blobContentDisposition(contentDisposition).blobContentEncoding(contentEncoding)
            .blobContentLanguage(contentLanguage).blobContentMD5(contentMD5).blobContentType(contentType),
            null, null).block()

        then:
        validateBlobProperties(bac.getPropertiesWithResponse(null).block(), cacheControl, contentDisposition, contentEncoding,
            contentLanguage, contentMD5, contentType == null ? "application/octet-stream" : contentType)
        // HTTP default content type is application/octet-stream.

        where:
        // The MD5 is simply set on the blob for commitBlockList, not validated.
        cacheControl | contentDisposition | contentEncoding | contentLanguage | contentMD5                                                   | contentType
        null         | null               | null            | null            | null                                                         | null
        "control"    | "disposition"      | "encoding"      | "language"      | MessageDigest.getInstance("MD5").digest(defaultData.array()) | "type"
    }

    @Unroll
    def "Buffered upload metadata"() {
        setup:
        Metadata metadata = new Metadata()
        if (key1 != null) {
            metadata.put(key1, value1)
        }
        if (key2 != null) {
            metadata.put(key2, value2)
        }

        when:
        bac.uploadWithResponse(Flux.just(getRandomData(10)), 10, 10, null, metadata, null).block()
        Response<BlobProperties> response = bac.getPropertiesWithResponse(null).block()

        then:
        response.statusCode() == 200
        response.value().metadata() == metadata

        where:
        key1  | value1 | key2   | value2
        null  | null   | null   | null
        "foo" | "bar"  | "fizz" | "buzz"
    }

    @Unroll
    def "Buffered upload AC"() {
        setup:
        bac.upload(defaultFlux, defaultDataSize).block()
        match = setupBlobMatchCondition(bac, match)
        leaseID = setupBlobLeaseCondition(bac, leaseID)
        def accessConditions = new BlobAccessConditions().modifiedAccessConditions(
            new ModifiedAccessConditions().ifModifiedSince(modified).ifUnmodifiedSince(unmodified)
                .ifMatch(match).ifNoneMatch(noneMatch))
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))

        expect:
        bac.uploadWithResponse(Flux.just(getRandomData(10)), 10, 2, null, null, accessConditions).block().statusCode() == 201

        where:
        modified | unmodified | match        | noneMatch   | leaseID
        null     | null       | null         | null        | null
        oldDate  | null       | null         | null        | null
        null     | newDate    | null         | null        | null
        null     | null       | receivedEtag | null        | null
        null     | null       | null         | garbageEtag | null
        null     | null       | null         | null        | receivedLeaseID
    }

    @Unroll
    def "Buffered upload AC fail"() {
        setup:
        bac.upload(defaultFlux, defaultDataSize).block()
        noneMatch = setupBlobMatchCondition(bac, noneMatch)
        leaseID = setupBlobLeaseCondition(bac, leaseID)
        BlobAccessConditions accessConditions = new BlobAccessConditions().modifiedAccessConditions(
            new ModifiedAccessConditions().ifModifiedSince(modified).ifUnmodifiedSince(unmodified)
                .ifMatch(match).ifNoneMatch(noneMatch))
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))

        when:
        bac.uploadWithResponse(Flux.just(getRandomData(10)), 10, 2, null, null, accessConditions).block()

        then:
        def e = thrown(StorageException)
        e.errorCode() == StorageErrorCode.CONDITION_NOT_MET ||
            e.errorCode() == StorageErrorCode.LEASE_ID_MISMATCH_WITH_BLOB_OPERATION

        where:
        modified | unmodified | match       | noneMatch    | leaseID
        newDate  | null       | null        | null         | null
        null     | oldDate    | null        | null         | null
        null     | null       | garbageEtag | null         | null
        null     | null       | null        | receivedEtag | null
        null     | null       | null        | null         | garbageLeaseID
    }

    // TODO:
    /*def "Upload NRF progress"() {
        setup:
        def data = getRandomData(BlockBlobURL.MAX_UPLOAD_BLOB_BYTES + 1)
        def numBlocks = data.remaining() / BlockBlobURL.MAX_STAGE_BLOCK_BYTES
        long prevCount = 0
        def mockReceiver = Mock(IProgressReceiver)


        when:
        TransferManager.uploadFromNonReplayableFlowable(Flowable.just(data), bu, BlockBlobURL.MAX_STAGE_BLOCK_BYTES, 10,
            new TransferManagerUploadToBlockBlobOptions(mockReceiver, null, null, null, 20)).blockingGet()
        data.position(0)

        then:
        // We should receive exactly one notification of the completed progress.
        1 * mockReceiver.reportProgress(data.remaining()) */

    /*
    We should receive at least one notification reporting an intermediary value per block, but possibly more
    notifications will be received depending on the implementation. We specify numBlocks - 1 because the last block
    will be the total size as above. Finally, we assert that the number reported monotonically increases.
     */
    /*(numBlocks - 1.._) * mockReceiver.reportProgress(!data.remaining()) >> { long bytesTransferred ->
        if (!(bytesTransferred > prevCount)) {
            throw new IllegalArgumentException("Reported progress should monotonically increase")
        } else {
            prevCount = bytesTransferred
        }
    }

    // We should receive no notifications that report more progress than the size of the file.
    0 * mockReceiver.reportProgress({ it > data.remaining() })
    notThrown(IllegalArgumentException)
}*/

    def "Buffered upload network error"() {
        setup:
        /*
         This test uses a Flowable that does not allow multiple subscriptions and therefore ensures that we are
         buffering properly to allow for retries even given this source behavior.
         */
        bac.upload(Flux.just(defaultData), defaultDataSize).block()
        def nonReplayableFlux = bac.download().block()

        // Mock a response that will always be retried.
        def mockHttpResponse = getStubResponse(500, new HttpRequest(HttpMethod.PUT, new URL("https://www.fake.com")))

        // Mock a policy that will always then check that the data is still the same and return a retryable error.
        def mockPolicy = Mock(HttpPipelinePolicy) {
            process(*_) >> { HttpPipelineCallContext context, HttpPipelineNextPolicy next ->
                return collectBytesInBuffer(context.httpRequest().body())
                    .map { b ->
                    return b == defaultData
                }
                .flatMap { b ->
                    if (b) {
                        return Mono.just(mockHttpResponse)
                    }
                    return Mono.error(new IllegalArgumentException())
                }
            }
        }

        // Build the pipeline
        bac = new BlobServiceClientBuilder()
            .credential(primaryCredential)
            .endpoint(String.format(defaultEndpointTemplate, primaryCredential.accountName()))
            .httpClient(getHttpClient())
            .httpLogDetailLevel(HttpLogDetailLevel.BODY_AND_HEADERS)
            .retryOptions(new RequestRetryOptions(null, 3, null, 500, 1500, null))
            .addPolicy(mockPolicy).buildAsyncClient()
            .getContainerAsyncClient(generateContainerName()).getBlockBlobAsyncClient(generateBlobName())

        when:
        // Try to upload the flowable, which will hit a retry. A normal upload would throw, but buffering prevents that.
        bac.upload(nonReplayableFlux, 1024, 4).block()
        // TODO: It could be that duplicates aren't getting made in the retry policy? Or before the retry policy?

        then:
        // A second subscription to a download stream will
        def e = thrown(StorageException)
        e.statusCode() == 500
    }

    def "Encryption not a no-op"() {
        setup:
        ByteBuffer byteBuffer = getRandomData(Constants.KB)
        def is = new ByteArrayInputStream(byteBuffer.array())
        def os = new ByteArrayOutputStream()

        when:
        bec.upload(is, Constants.KB)
        cc.getBlobClient(URLParser.parse(bec.getBlobUrl()).blobName()).download(os)

        ByteBuffer outputByteBuffer = ByteBuffer.wrap(os.toByteArray())

        then:
        outputByteBuffer.array() != byteBuffer.array()
    }

    @Unroll
    def "Encryption"() {
        when:
        def byteBufferList = [];

        /*
        Sending a sequence of buffers allows us to test encryption behavior in different cases when the buffers do
        or do not align on encryption boundaries.
         */
        for (def i = 0; i < byteBufferCount; i++) {
            byteBufferList.add(getRandomData(size))
        }
        def flux = Flux.fromIterable(byteBufferList)

        // Test basic upload.
        beac.upload(flux, size * byteBufferCount).block()
        ByteBuffer outputByteBuffer = collectBytesInBuffer(beac.download().block()).block()

        then:
        compareListToBuffer(byteBufferList, outputByteBuffer)

        when:
        // Test buffered upload.
        beac.upload(flux, size, 2).block()
        outputByteBuffer = collectBytesInBuffer(beac.download().block()).block()

        then:
        compareListToBuffer(byteBufferList, outputByteBuffer)

        where:
        size              | byteBufferCount
        5                 | 2                 // 0 Two buffers smaller than an encryption block.
        8                 | 2                 // 1 Two buffers that equal an encryption block.
        10                | 1                 // 2 One buffer smaller than an encryption block.
        10                | 2                 // 3 A buffer that spans an encryption block.
        16                | 1                 // 4 A buffer exactly the same size as an encryption block.
        16                | 2                 // 5 Two buffers the same size as an encryption block.
        20                | 1                 // 6 One buffer larger than an encryption block.
        20                | 2                 // 7 Two buffers larger than an encryption block.
        100               | 1                 // 8 One buffer containing multiple encryption blocks
        5 * Constants.KB  | Constants.KB      // 9 Large number of small buffers.
        10 * Constants.MB | 2                 // 10 Small number of large buffers.
    }

    @Unroll
    def "Encryption headers"() {
        setup:
        BlobHTTPHeaders headers = new BlobHTTPHeaders().blobCacheControl(cacheControl)
            .blobContentDisposition(contentDisposition)
            .blobContentEncoding(contentEncoding)
            .blobContentLanguage(contentLanguage)
            .blobContentMD5(contentMD5)
            .blobContentType(contentType)

        when:
        beac.uploadWithResponse(defaultFlux, defaultDataSize, headers, null, null).block()
        def response = beac.getPropertiesWithResponse(null).block()

        then:
        response.statusCode() == 200
        validateBlobProperties(response, cacheControl, contentDisposition, contentEncoding, contentLanguage,
            contentMD5, contentType == null ? "application/octet-stream" : contentType)
        // HTTP default content type is application/octet-stream

        when:
        beac.uploadWithResponse(defaultFlux, defaultDataSize, 2, headers, null, null).block()
        response = beac.getPropertiesWithResponse(null).block()

        then:
        response.statusCode() == 200
        validateBlobProperties(response, cacheControl, contentDisposition, contentEncoding, contentLanguage,
            contentMD5, contentType == null ? "application/octet-stream" : contentType)
        // HTTP default content type is application/octet-stream

        where:
        cacheControl | contentDisposition | contentEncoding | contentLanguage | contentMD5                                                   | contentType
        null         | null               | null            | null            | null                                                         | null
        "control"    | "disposition"      | "encoding"      | "language"      | MessageDigest.getInstance("MD5").digest(defaultData.array()) | "type"
    }

    @Unroll
    def "Encryption metadata"() {
        setup:
        Metadata metadata = new Metadata()
        if (key1 != null) {
            metadata.put(key1, value1)
        }
        if (key2 != null) {
            metadata.put(key2, value2)
        }

        when:
        beac.uploadWithResponse(defaultFlux, defaultDataSize, null, metadata, null).block()
        def properties = beac.getProperties().block()

        then:
        properties.metadata() == metadata

        where:
        key1  | value1 | key2   | value2
        null  | null   | null   | null
        "foo" | "bar"  | "fizz" | "buzz"
    }

    @Unroll
    def "Encryption AC"() {
        setup:
        beac.upload(defaultFlux, defaultDataSize).block()
        match = setupBlobMatchCondition(beac, match)
        leaseID = setupBlobLeaseCondition(beac, leaseID)
        BlobAccessConditions bac = new BlobAccessConditions().modifiedAccessConditions(
            new ModifiedAccessConditions().ifModifiedSince(modified).ifUnmodifiedSince(unmodified)
                .ifMatch(match).ifNoneMatch(noneMatch))
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))

        expect:
        beac.uploadWithResponse(defaultFlux, defaultDataSize, null, null, bac).block().statusCode() == 201

        where:
        modified | unmodified | match        | noneMatch   | leaseID
        null     | null       | null         | null        | null
        oldDate  | null       | null         | null        | null
        null     | newDate    | null         | null        | null
        null     | null       | receivedEtag | null        | null
        null     | null       | null         | garbageEtag | null
        null     | null       | null         | null        | receivedLeaseID
    }

    @Unroll
    def "Encryption AC fail"() {
        setup:
        beac.upload(defaultFlux, defaultDataSize).block()
        noneMatch = setupBlobMatchCondition(beac, noneMatch)
        setupBlobLeaseCondition(beac, leaseID)
        BlobAccessConditions bac = new BlobAccessConditions().modifiedAccessConditions(
            new ModifiedAccessConditions().ifModifiedSince(modified).ifUnmodifiedSince(unmodified)
                .ifMatch(match).ifNoneMatch(noneMatch))
            .leaseAccessConditions(new LeaseAccessConditions().leaseId(leaseID))

        when:
        beac.uploadWithResponse(defaultFlux, defaultDataSize, null, null, bac).block()

        then:
        def e = thrown(StorageException)
        e.errorCode() == StorageErrorCode.CONDITION_NOT_MET ||
            e.errorCode() == StorageErrorCode.LEASE_ID_MISMATCH_WITH_BLOB_OPERATION
        where:
        modified | unmodified | match       | noneMatch    | leaseID
        newDate  | null       | null        | null         | null
        null     | oldDate    | null        | null         | null
        null     | null       | garbageEtag | null         | null
        null     | null       | null        | receivedEtag | null
        null     | null       | null        | null         | garbageLeaseID
    }

    // Progress tests. Tests with parallel upload/download. Stream retries.

    // Options: Pass a policy and no key on encryption--throw; no encryption policy in construction-> throw
    // Construct a policy and require encryption=true and no key or resolver -> throw

    // TODO: Document which tests are testing which cases. Ensure that some don't align along blocks. Maybe have a mock flowable that returns some really smally byteBuffers.
    // Request one byte. Test key resolver. Lots more. Require encryption tests (and downloading blobs that aren't encryption, esp. ones that are smaller than what the expanded range would try).
    // Samples. API refs. Reliable download.
    // Test EncryptedBlobRange
    // One blob sample is failing in the onErrorResumeNext case because it gets weird with the generics and downloadResponse

    @Unroll
    def "Small blob tests"(int offset, Integer count, int size, int status) {
        when:
        ByteBuffer byteBuffer = getRandomData(size)

        def flux = Flux.just(byteBuffer)

        beac.upload(flux, size).block()

        def downloadResponse = beac.downloadWithResponse(
            new BlobRange(offset.longValue(), count), null, null, false).block()

        ByteBuffer outputByteBuffer = collectBytesInBuffer(downloadResponse.value()).block()

        and:
        def limit
        if (count != null) {
            if (count < byteBuffer.capacity()) {
                limit = offset + count
            } else {
                limit = byteBuffer.capacity()
            }
        } else {
            limit = size
        }
        byteBuffer.position(offset).limit(limit) // reset the position after the read in upload.

        then:
        downloadResponse.statusCode() == status
        byteBuffer == outputByteBuffer

        where:
        offset | count | size | status // note
        0      | null  | 10   | 200 // 0
        3      | null  | 10   | 200 // 1
        0      | 10    | 10   | 206 // 2
        0      | 16    | 10   | 206 // 3
        3      | 16    | 10   | 206 // 4
        0      | 7     | 10   | 206 // 5
        3      | 7     | 10   | 206 // 6
        3      | 3     | 10   | 206 // 7
        0      | null  | 16   | 200 // 8
        5      | null  | 16   | 200 // 9
        0      | 16    | 16   | 206 // 10
        0      | 20    | 16   | 206 // 11
        5      | 20    | 16   | 206 // 12
        5      | 11    | 16   | 206 // 13
        5      | 7     | 16   | 206 // 14
        0      | null  | 24   | 200 // 15
        5      | null  | 24   | 200 // 16
        0      | 24    | 24   | 206 // 17
        5      | 24    | 24   | 206 // 18
        0      | 30    | 24   | 206 // 19
        5      | 19    | 24   | 206 // 20
        5      | 10    | 24   | 206 // 21
    }

    // Keep the small and large blob tests but combine them into Range tests. Looks like some pattern of:
    // Small, full blob, no offst; small, full blob, offset; small full blob count; ... blob size of block... blob bigger than block... etc.

    @Unroll
    def "Large Blob Tests"() {
        when:
        ByteBuffer byteBuffer = getRandomData(size)

        Flux<ByteBuffer> flux = Flux.just(byteBuffer)

       beac.upload(flux, size).block()

        def downloadResponse = beac.downloadWithResponse(new BlobRange(offset.longValue(),count), null, null, false)
            .block()

        def outputByteBuffer = collectBytesInBuffer(downloadResponse.value()).block()

        byte[] expectedByteArray = Arrays.copyOfRange(byteBuffer.array(), (int) offset, (int) (calcUpperBound(offset, count, size)))

        then:
        outputByteBuffer.array() == expectedByteArray

        where:
        offset          | count             | size          // note
        0L              | null              | 20 * KB       // 0
        5L              | null              | 20 * KB       // 1
        16L             | null              | 20 * KB       // 2
        24L             | null              | 20 * KB       // 3
        500             | null              | 20 * KB       // 4
        5000            | null              | 20 * KB       // 5
        0L              | 5L                | 20 * KB       // 6
        0L              | 16L               | 20 * KB       // 7
        0L              | 24L               | 20 * KB       // 8
        0L              | 500L              | 20 * KB       // 9
        0L              | 5000L             | 20 * KB       // 10
        0L              | 25 * KB           | 20 * KB       // 11
        0L              | 20 * KB           | 20 * KB       // 12
        5L              | 25 * KB           | 20 * KB       // 13
        5L              | 20 * KB - 5       | 20 * KB       // 14
        5L              | 20 * KB - 10      | 20 * KB       // 15
        5L              | 20 * KB - 20      | 20 * KB       // 16
        16L             | 20 * KB - 16      | 20 * KB       // 17
        16L             | 20 * KB           | 20 * KB       // 18
        16L             | 20 * KB - 20      | 20 * KB       // 19
        16L             | 20 * KB - 32      | 20 * KB       // 20
        500L            | 500L              | 20 * KB       // 21
        500L            | 20 * KB - 500     | 20 * KB       // 22
        20 * KB - 5     | 5                 | 20 * KB       // 23
        0L              | null              | 20 * KB + 8   // 24
        5L              | null              | 20 * KB + 8   // 25
        16L             | null              | 20 * KB + 8   // 26
        24L             | null              | 20 * KB + 8   // 27
        500             | null              | 20 * KB + 8   // 28
        5000            | null              | 20 * KB + 8   // 29
        0L              | 5L                | 20 * KB + 8   // 30
        0L              | 16L               | 20 * KB + 8   // 31
        0L              | 24L               | 20 * KB + 8   // 32
        0L              | 500L              | 20 * KB + 8   // 33
        0L              | 5000L             | 20 * KB + 8   // 34
        0L              | 20 * KB + 8       | 20 * KB + 8   // 35
        0L              | 20 * KB + 8       | 20 * KB + 8   // 36
        5L              | 20 * KB + 8 - 5   | 20 * KB + 8   // 37
        5L              | 20 * KB + 8 - 5   | 20 * KB + 8   // 38
        5L              | 20 * KB + 8 - 10  | 20 * KB + 8   // 39
        5L              | 20 * KB + 8 - 20  | 20 * KB + 8   // 40
        16L             | 20 * KB + 8 - 16  | 20 * KB + 8   // 41
        16L             | 20 * KB + 8       | 20 * KB + 8   // 42
        16L             | 20 * KB + 8 - 20  | 20 * KB + 8   // 43
        16L             | 20 * KB + 8 - 32  | 20 * KB + 8   // 44
        500L            | 500L              | 20 * KB + 8   // 45
        500L            | 20 * KB + 8 - 500 | 20 * KB + 8   // 46
        20 * KB + 8 - 5 | 5                 | 20 * KB + 8   // 47
    }

    @Unroll
    def "Block block cross platform decryption tests"() {
        when:
        List<TestEncryptionBlob> list = getTestData("encryptedBlob.json")
        symmetricKey = new SymmetricKey("symmKey1", Base64.getDecoder().decode(list.get(index).getKey()))
        blobEncryptionPolicy = new BlobEncryptionPolicy(symmetricKey, null, false)
        beac = ccAsync.getBlockBlobAsyncClient(URLParser.parse(bac.getBlobUrl()).blobName(), null, blobEncryptionPolicy)

        byte[] encryptedBytes = Base64.getDecoder().decode(list.get(index).getEncryptedContent())
        byte[] decryptedBytes = Base64.getDecoder().decode(list.get(index).getDecryptedContent())

        Metadata metadata = new Metadata()

        ObjectMapper objectMapper = new ObjectMapper()
        metadata.put(Constants.EncryptionConstants.ENCRYPTION_DATA_KEY, objectMapper.writeValueAsString(list.get(index).getEncryptionData()))

        bac.uploadWithResponse(Flux.just(ByteBuffer.wrap(encryptedBytes)), encryptedBytes.length, null, metadata, null).block()


        ByteBuffer outputByteBuffer = collectBytesInBuffer(beac.download().block()).block()

        then:
        outputByteBuffer.array() == decryptedBytes

        where:
        index << [0, 1, 2, 3, 4]
    }


    def calcUpperBound(Long offset, Long count, Long size) {
        if (count == null || offset + count > size) {
            return size
        }
        return offset + count
    }

    def getTestData(String fileName) {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI())
        String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
        ObjectMapper mapper = new ObjectMapper()
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, TestEncryptionBlob.class)
        List<TestEncryptionBlob> list = mapper.readValue(json, collectionType)
        return list
    }

    // TODO: upload and buffered upload tests and upload from file and upload all blob types and BlobInputStream and OutputStream
}
