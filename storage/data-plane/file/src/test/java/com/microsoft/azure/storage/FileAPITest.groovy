package com.microsoft.azure.storage

import com.microsoft.azure.storage.file.*
import com.microsoft.azure.storage.file.models.*
import com.microsoft.rest.v2.Context
import com.microsoft.rest.v2.http.HttpPipeline
import com.microsoft.rest.v2.util.FlowableUtil
import io.reactivex.Flowable
import spock.lang.Unroll

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.time.OffsetDateTime

class FileAPITest extends APISpec{
    DirectoryURL directoryUrl
    FileURL fileUrl

    def setup() {
        directoryUrl = shu.createDirectoryURL(generateDirectoryName())
        directoryUrl.create(null, null).blockingGet()
        fileUrl = directoryUrl.createFileURL(generateFileName())
    }

    def "File create"(){
        when:
        FileCreateResponse response = fileUrl.create(10, null, null, null).blockingGet()

        then:
        response.statusCode() == 201
        validateResponseHeaders(response.headers())
    }

    def "File create metadata"(){
        setup:
        Metadata metadata = new Metadata()
        metadata.put("Key1", "Value1")
        metadata.put("Key2", "Value2")
        FileCreateResponse createResponse = fileUrl.create(10, null, metadata, null).blockingGet()

        when:
        FileGetPropertiesResponse propertiesResponse = fileUrl.getProperties(null).blockingGet()

        then:
        createResponse.statusCode() == 201
        validateResponseHeaders(createResponse.headers())
        propertiesResponse.statusCode() == 200
        validateResponseHeaders(propertiesResponse.headers())
        propertiesResponse.headers().metadata() == metadata
    }

    def "File create http headers"(){
        setup:
        def basicHeader = new FileHTTPHeaders().withFileContentType(contentType)
                                                .withFileContentDisposition(contentDisposition)
                                                .withFileCacheControl(cacheControl)
                                                .withFileContentMD5(contentMD5)
                                                .withFileContentLanguage(contentLanguage)
                                                .withFileContentEncoding(contentEncoding)

        FileCreateResponse createResponse = fileUrl.create(10, basicHeader,null, null).blockingGet()

        when:
        FileGetPropertiesResponse propertiesResponse = fileUrl.getProperties(null).blockingGet()

        then:
        createResponse.statusCode() == 201
        validateResponseHeaders(createResponse.headers())
        propertiesResponse.statusCode() == 200
        validateResponseHeaders(propertiesResponse.headers())
        propertiesResponse.headers().contentType() == (contentType == null ? "application/octet-stream" : contentType)
        propertiesResponse.headers().contentDisposition() == contentDisposition
        propertiesResponse.headers().cacheControl() == cacheControl
        propertiesResponse.headers().contentMD5() == contentMD5
        propertiesResponse.headers().contentLanguage() == contentLanguage
        propertiesResponse.headers().contentEncoding() == contentEncoding

        where:
        contentType | contentDisposition    | cacheControl  | contentMD5                                                                                    | contentLanguage   | contentEncoding
        "my-type"   | null                  | null          |    null                                                                                       | null              | null
        "my-type"   | "my_disposition"      | "my_cache"    | MessageDigest.getInstance("MD5").digest("md5String".getBytes("UTF-8"))   | "my_language"     | "my_encoding"
    }

    def "File create error"(){
        setup:
        directoryUrl = shu.createDirectoryURL(generateDirectoryName())
        fileUrl = directoryUrl.createFileURL(generateFileName())

        when:
        fileUrl.create(10, null, null, null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.statusCode() == 404
        e.errorCode() == StorageErrorCode.PARENT_NOT_FOUND
    }

    def "File create context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(201, FileCreateHeaders)))
        fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.create(10, null, null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "File upload range"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        def range = new FileRange().withOffset(0).withCount(defaultFileSize)
        FileUploadRangeResponse response = fileUrl.uploadRange(range, Flowable.just(defaultData), null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 201
    }

    def "File upload range negative offset"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        def range = new FileRange().withOffset(-2).withCount(defaultFileSize)
        FileUploadRangeResponse response = fileUrl.uploadRange(range, Flowable.just(defaultData), null).blockingGet()

        then:
        thrown(IllegalArgumentException)
    }

    def "File upload range empty body"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        def range = new FileRange().withOffset(0).withCount(0)
        FileUploadRangeResponse response = fileUrl.uploadRange(range, Flowable.just(ByteBuffer.wrap(new byte[0])), null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.statusCode() == 400
        e.errorCode() == StorageErrorCode.INVALID_HEADER_VALUE
    }

    def "File upload range null body"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        FileUploadRangeResponse response = fileUrl.uploadRange(null, Flowable.just(null), null).blockingGet()

        then:
        thrown(NullPointerException)
    }

    def "File upload range error"(){
        when:
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(defaultFileSize), Flowable.just(defaultData), null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "File uploadRange context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(201, FileUploadRangeHeaders)))
        fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.create(defaultFileSize, null, null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "File clear all range"(){
        setup:
        fileUrl.create(512, null, null, null).blockingGet()
        def range = new FileRange().withOffset(0).withCount(512)
        fileUrl.uploadRange(range, Flowable.just(getRandomData(512)), null).blockingGet()

        when:
        FileUploadRangeResponse response = fileUrl.clearRange(range, null).blockingGet()
        FileGetRangeListResponse rangeListResponse = fileUrl.getRangeList(FileRange.DEFAULT, null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 201
        rangeListResponse.body().size() == 0
    }

    def "File clear some range"(){
        setup:
        fileUrl.create(2048*2, null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(1024), Flowable.just(getRandomData(1024)), null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(1024).withCount(1024), Flowable.just(getRandomData(1024)), null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(3072).withCount(1024), Flowable.just(getRandomData(1024)), null).blockingGet()

        when:
        fileUrl.clearRange(new FileRange().withOffset(0).withCount(1024), null).blockingGet()
        FileGetRangeListResponse response = fileUrl.getRangeList(new FileRange().withOffset(0).withCount(0), null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 200
        response.body().size() == 2
        assert (response.body().get(0).start() == 1024 && response.body().get(0).end()==2047)
        assert (response.body().get(1).start() == 3072 && response.body().get(1).end()==4095)
    }

    def "File unaligned clear range"(){
        setup:
        fileUrl.create(1, null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(1), Flowable.just(getRandomData(1)), null).blockingGet()

        when:
        fileUrl.clearRange(new FileRange().withOffset(0).withCount(1), null).blockingGet()
        FileGetRangeListResponse response = fileUrl.getRangeList(new FileRange().withOffset(0).withCount(0), null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 200
        response.body().size() == 1
    }

    def "File clear range negative offset"(){
        setup:
        fileUrl.create(1, null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(1), Flowable.just(getRandomData(1)), null).blockingGet()

        when:
        fileUrl.clearRange(new FileRange().withOffset(-1).withCount(1), null).blockingGet()

        then:
        thrown(IllegalArgumentException)
    }

    def "File clear range error"(){
        when:
        fileUrl.clearRange(new FileRange().withOffset(0).withCount(defaultFileSize),null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "Set metadata all null"() {
        when:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()
        FileSetMetadataResponse response = fileUrl.setMetadata(null, null).blockingGet()

        then:
        fileUrl.getProperties(null).blockingGet().headers().metadata().size() == 0
        validateBasicHeaders(response.headers())
        response.headers().isServerEncrypted()
    }

    @Unroll
    def "Set metadata metadata"() {
        setup:
        Metadata metadata = new Metadata()
        if (key1 != null && value1 != null) {
            metadata.put(key1, value1)
        }
        if (key2 != null && value2 != null) {
            metadata.put(key2, value2)
        }
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        expect:
        fileUrl.setMetadata(metadata, null).blockingGet().statusCode() == statusCode
        fileUrl.getProperties(null).blockingGet().headers().metadata() == metadata

        where:
        key1  | value1 | key2   | value2 || statusCode
        null  | null   | null   | null   || 200
        "foo" | "bar"  | "fizz" | "buzz" || 200
    }

    def "Set metadata error"() {
        when:
        fileUrl.setMetadata(null, null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "Set metadata context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, FileSetMetadataHeaders)))
        fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.create(defaultFileSize, null, null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Set HTTP headers null"() {
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()
        FileSetHTTPHeadersResponse response = fileUrl.setHTTPHeaders(null, null).blockingGet()

        expect:
        response.statusCode() == 200
        validateResponseHeaders(response.headers())
    }

    @Unroll
    def "Set HTTP headers headers"() {
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()
        FileHTTPHeaders putHeaders = new FileHTTPHeaders().withFileCacheControl(cacheControl)
                .withFileContentDisposition(contentDisposition)
                .withFileContentEncoding(contentEncoding)
                .withFileContentLanguage(contentLanguage)
                .withFileContentMD5(contentMD5)
                .withFileContentType(contentType)
        fileUrl.setHTTPHeaders(putHeaders, null).blockingGet()

        FileGetPropertiesHeaders receivedHeaders =
                fileUrl.getProperties(null).blockingGet().headers()

        expect:
        validateFileHeaders(receivedHeaders, cacheControl, contentDisposition, contentEncoding, contentLanguage,
                contentMD5, contentType)

        where:
        cacheControl | contentDisposition | contentEncoding | contentLanguage | contentMD5                                                                               | contentType
        null         | null               | null            | null            | null                                                                                     | null
        "control"    | "disposition"      | "encoding"      | "language"      | Base64.getEncoder().encode(MessageDigest.getInstance("MD5").digest(defaultData.array())) | "type"

    }

    def "Set HTTP headers error"() {
        setup:
        fileUrl = directoryUrl.createFileURL(generateFileName())

        when:
        fileUrl.setHTTPHeaders(null,null).blockingGet()

        then:
        thrown(StorageException)
    }

    def "Set HTTP headers context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, FileSetHTTPHeadersHeaders)))

        fileUrl = fileUrl.withPipeline(pipeline)

        when:
        fileUrl.setHTTPHeaders(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Get properties all null"() {
        when:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()
        FileGetPropertiesHeaders headers = fileUrl.getProperties(null).blockingGet().headers()

        then:
        validateResponseHeaders(headers)
        headers.metadata().isEmpty()
        headers.fileType() == "File"
        headers.copyCompletionTime() == null // tested in "copy"
        headers.copyStatusDescription() == null // only returned when the service has errors; cannot validate.
        headers.copyId() == null // tested in "abort copy"
        headers.copyProgress() == null // tested in "copy"
        headers.copySource() == null // tested in "copy"
        headers.copyStatus() == null // tested in "copy"
        headers.contentLength() != null
        headers.contentType() != null
        headers.contentMD5() == null
        headers.contentEncoding() == null // tested in "set HTTP headers"
        headers.contentDisposition() == null // tested in "set HTTP headers"
        headers.contentLanguage() == null // tested in "set HTTP headers"
        headers.cacheControl() == null // tested in "set HTTP headers"
        headers.isServerEncrypted()
    }

    def "Get properties error"() {
        setup:
        fileUrl = directoryUrl.createFileURL(generateFileName())

        when:
        fileUrl.getProperties(null).blockingGet()

        then:
        thrown(StorageException)
    }

    def "Get properties context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, FileGetPropertiesHeaders)))

        fileUrl = fileUrl.withPipeline(pipeline)

        when:
        fileUrl.getProperties(defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Copy"() {
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(defaultFileSize), Flowable.just(defaultData), null).blockingGet()
        FileURL fu2 = directoryUrl.createFileURL(generateFileName())
        FileStartCopyHeaders headers =
                fu2.startCopy(fileUrl.toURL(), null, null).blockingGet().headers()

        when:
        while (fu2.getProperties(null).blockingGet().headers().copyStatus() == CopyStatusType.PENDING) {
            sleep(1000)
        }
        FileGetPropertiesHeaders headers2 = fu2.getProperties(null).blockingGet().headers()

        then:
        headers2.copyStatus() == CopyStatusType.SUCCESS
        headers2.copyCompletionTime() != null
        headers2.copyProgress() != null
        headers2.copySource() != null
        validateBasicHeaders(headers)
        headers.copyId() != null
    }

    @Unroll
    def "Copy metadata"() {
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(defaultFileSize), Flowable.just(defaultData), null).blockingGet()
        FileURL fu2 = directoryUrl.createFileURL(generateFileName())
        Metadata metadata = new Metadata()
        if (key1 != null && value1 != null) {
            metadata.put(key1, value1)
        }
        if (key2 != null && value2 != null) {
            metadata.put(key2, value2)
        }

        FileStartCopyResponse response =
                fu2.startCopy(fileUrl.toURL(), metadata, null).blockingGet()
        waitForCopy(fu2, response.headers().copyStatus())

        expect:
        FileGetPropertiesResponse resp = fu2.getProperties(null).blockingGet()
        resp.headers().metadata() == metadata

        where:
        key1  | value1 | key2   | value2 || statusCode
        null  | null   | null   | null   || 200
        "foo" | "bar"  | "fizz" | "buzz" || 200
    }

    def "Copy error"() {
        when:
        fileUrl.startCopy(new URL("http://www.error.com"), null,null).blockingGet()

        then:
        thrown(StorageException)
    }

    def "Copy context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(202, FileStartCopyHeaders)))

        fileUrl = fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.startCopy(new URL("http://www.example.com"), null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Abort copy"() {
        setup:
        def shareName = generateShareName()
        shu = primaryServiceURL.createShareURL(shareName)
        shu.create().blockingGet()
        def fileName = generateFileName()
        def fu = shu.createFileURL(fileName)
        // Create the Service SAS Signature
        def v = new ServiceSASSignatureValues()
        def p = new ShareSASPermission()
                .withRead(true)
                .withWrite(true)
                .withCreate(true)
                .withDelete(true)

        v.withPermissions(p.toString())
                .withStartTime(OffsetDateTime.now().minusDays(1))
                .withExpiryTime(OffsetDateTime.now().plusDays(1))
                .withShareName(shareName)
                .withFilePath(fileName)
        // Convert the fileURL into fileURL parts and add SASQueryParameter to the fileURL
        FileURLParts parts = URLParser.parse(fu.toURL())
        parts.withSasQueryParameters(v.generateSASQueryParameters(primaryCreds)).withScheme("https")
        fu = new FileURL(parts.toURL(), StorageURL.createPipeline(new AnonymousCredentials(),
                new PipelineOptions()))
        // Create a large file that takes time to copy
        fu.create(1000 * 1024 * 1024, null, null,null).blockingGet()
        fu.uploadRange(new FileRange().withOffset(0).withCount(4 * 1024 * 1024), Flowable.just(getRandomData(4 * 1024 * 1024)), null)
        fu.uploadRange(new FileRange().withOffset(4 * 1024 * 1024).withCount(4 * 1024 * 1024), Flowable.just(getRandomData(4 * 1024 * 1024)), null)
        fu.uploadRange(new FileRange().withOffset(96 * 1024 * 1024).withCount(4 * 1024 * 1024), Flowable.just(getRandomData(4 * 1024 * 1024)), null)

        // Create a share into a secondary account, so test gets enough time to abort copy operation.
        def shu2 = alternateServiceURL.createShareURL(generateShareName())
        shu2.create(null, null, null).blockingGet()
        def fu2 = shu2.createFileURL(generateFileName())

        when:
        String copyId =
                fu2.startCopy(fu.toURL(), null, null).blockingGet().headers().copyId()
        FileAbortCopyResponse response = fu2.abortCopy(copyId,null).blockingGet()

        then:
        response.statusCode() == 204
        validateBasicHeaders(response.headers())

        cleanup:
        // Normal test cleanup will not clean up containers in the alternate account.
        fu2.delete(null).blockingGet().statusCode() == 202
        shu2.delete().blockingGet()
    }

    def "Abort copy error"() {
        when:
        fileUrl.abortCopy("id", null).blockingGet()

        then:
        thrown(StorageException)
    }

    def "Abort copy context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(204, FileAbortCopyHeaders)))

        fileUrl = fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.abortCopy("id", defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "File download all null"() {
        setup:
        fileUrl.create(defaultText.size(), null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(defaultText.size()), Flowable.just(defaultData), null).blockingGet()
        when:
        DownloadResponse response = fileUrl.download(null, false, null)
                .blockingGet()
        ByteBuffer body = FlowableUtil.collectBytesInBuffer(response.body()).blockingGet()
        FileDownloadHeaders headers = response.headers()

        then:
        validateResponseHeaders(headers)
        body == defaultData
        headers.metadata().isEmpty()
        headers.contentLength() != null
        headers.contentType() != null
        headers.contentRange() == null
        headers.contentMD5() == null
        headers.contentEncoding() == null
        headers.cacheControl() == null
        headers.contentDisposition() == null
        headers.contentLanguage() == null
        headers.copyCompletionTime() == null
        headers.copyStatusDescription() == null
        headers.copyId() == null
        headers.copyProgress() == null
        headers.copySource() == null
        headers.copyStatus() == null
        headers.acceptRanges() == "bytes"
        headers.serverEncrypted
    }

    def "File download range"() {
        setup:
        fileUrl.create(defaultText.size(), null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(defaultText.size()), Flowable.just(defaultData), null).blockingGet()
        FileRange range = new FileRange().withOffset(offset).withCount(count)

        when:
        ByteBuffer body = FlowableUtil.collectBytesInBuffer(
                fileUrl.download(range, false, null).blockingGet().body()).blockingGet()
        String bodyStr = new String(body.array())

        then:
        bodyStr == expectedData

        where:
        offset | count || expectedData
        0      | null  || defaultText
        0      | 5     || defaultText.toString().substring(0, 5)
        3      | 2     || defaultText.toString().substring(3, 5)
    }

    def "File download md5"() {
        setup:
        fileUrl.create(defaultText.size(), null, null, null).blockingGet()
        fileUrl.uploadRange(new FileRange().withOffset(0).withCount(defaultText.size()), Flowable.just(defaultData), null).blockingGet()

        expect:
        fileUrl.download(new FileRange().withOffset(0).withCount(3),true, null).blockingGet()
                .headers().contentMD5() ==
                MessageDigest.getInstance("MD5").digest(defaultText.substring(0, 3).getBytes())
    }

    def "File download error"() {
        setup:
        fileUrl = shu.createFileURL(generateFileName())

        when:
        fileUrl.download(null, false, null).blockingGet()

        then:
        thrown(StorageException)
    }

    def "file download context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(206, FileDownloadHeaders)))

        fileUrl = fileUrl.withPipeline(pipeline)

        when:
        fileUrl.download(null, false, defaultContext).blockingGet()

        then:
        notThrown(StorageException)
    }

    def "File resize increase file size"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        fileUrl.resize(defaultFileSize * 2, null).blockingGet()
        FileGetPropertiesResponse response = fileUrl.getProperties(null).blockingGet()

        then:
        response.statusCode() == 200
        validateResponseHeaders(response.headers())
        response.headers().contentLength() == defaultFileSize * 2
    }

    def "File resize zero"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        fileUrl.resize(0, null).blockingGet()
        FileGetPropertiesResponse response = fileUrl.getProperties(null).blockingGet()

        then:
        response.statusCode() == 200
        validateResponseHeaders(response.headers())
        response.headers().contentLength() == 0
    }

    def "File resize invalid size"(){
        setup:
        fileUrl.create(defaultFileSize, null, null, null).blockingGet()

        when:
        fileUrl.resize(-4, null).blockingGet()
        FileGetPropertiesResponse response = fileUrl.getProperties(null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.statusCode() == 400
        e.errorCode() == StorageErrorCode.INVALID_HEADER_VALUE
    }

    def "file resize context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, FileSetHTTPHeadersHeaders)))

        fileUrl = fileUrl.withPipeline(pipeline)

        when:
        fileUrl.resize(defaultFileSize, defaultContext).blockingGet()

        then:
        notThrown(StorageException)
    }

    // We aren't able to open handles via REST, so we are only testing the 0 handles case.
    def "File list handles min"(){
        setup:
        fileUrl.create(10, null, null, null).blockingGet()

        when:
        FileListHandlesResponse response = fileUrl.listHandles(null).blockingGet()

        then:
        response.statusCode() == 200
        response.body().nextMarker() == null
        response.body().handleList().size() == 0
        validateBasicHeaders(response.headers())
    }

    def "File list handles context"(){
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, FileListHandlesHeaders)))
        fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.create(defaultFileSize, null, null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "File list handles error"(){
        when:
        fileUrl.listHandles(null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "File list handles"(){
        setup:
        fileUrl.create(10, null, null, null).blockingGet()

        when:
        FileListHandlesResponse response = fileUrl.listHandles(null, null, Context.NONE).blockingGet()

        then:
        response.statusCode() == 200
        response.body().nextMarker() == null
        response.body().handleList().size() == 0
        validateBasicHeaders(response.headers())
    }

    def "File force close handles min"(){
        setup:
        fileUrl.create(10, null, null, null).blockingGet()

        when:
        FileForceCloseHandlesResponse response = fileUrl.forceCloseHandles(null).blockingGet()

        then:
        response.statusCode() == 200
        response.headers().marker() == null
        response.headers().numberOfHandlesClosed() == 0
        validateBasicHeaders(response.headers())
    }

    def "File force close handles context"(){
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, FileForceCloseHandlesHeaders)))
        fileUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        fileUrl.create(defaultFileSize, null, null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "File force close handles error"(){
        when:
        fileUrl.forceCloseHandles(null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "File force close handles"(){
        setup:
        fileUrl.create(10, null, null, null).blockingGet()

        when:
        FileForceCloseHandlesResponse response = fileUrl.forceCloseHandles(FileURL.ALL_HANDLES, null, Context.NONE).blockingGet()

        then:
        response.statusCode() == 200
        response.headers().marker() == null
        response.headers().numberOfHandlesClosed() == 0
        validateBasicHeaders(response.headers())
    }

}
