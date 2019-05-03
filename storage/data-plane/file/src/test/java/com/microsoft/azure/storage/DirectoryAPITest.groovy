package com.microsoft.azure.storage

import com.microsoft.azure.storage.file.DirectoryURL
import com.microsoft.azure.storage.file.Metadata
import com.microsoft.azure.storage.file.StorageException
import com.microsoft.azure.storage.file.models.DirectoryCreateHeaders
import com.microsoft.azure.storage.file.models.DirectoryCreateResponse
import com.microsoft.azure.storage.file.models.DirectoryDeleteHeaders
import com.microsoft.azure.storage.file.models.DirectoryDeleteResponse
import com.microsoft.azure.storage.file.models.DirectoryForceCloseHandlesHeaders
import com.microsoft.azure.storage.file.models.DirectoryForceCloseHandlesResponse
import com.microsoft.azure.storage.file.models.DirectoryGetPropertiesHeaders
import com.microsoft.azure.storage.file.models.DirectoryGetPropertiesResponse
import com.microsoft.azure.storage.file.models.DirectoryItem
import com.microsoft.azure.storage.file.models.DirectoryListFilesAndDirectoriesSegmentResponse
import com.microsoft.azure.storage.file.models.DirectoryListHandlesHeaders
import com.microsoft.azure.storage.file.models.DirectoryListHandlesResponse
import com.microsoft.azure.storage.file.models.DirectorySetMetadataHeaders
import com.microsoft.azure.storage.file.models.DirectorySetMetadataResponse
import com.microsoft.azure.storage.file.models.StorageErrorCode
import com.microsoft.rest.v2.Context
import com.microsoft.rest.v2.http.HttpPipeline
import spock.lang.Unroll

class DirectoryAPITest extends APISpec {

    DirectoryURL directoryUrl

    def setup() {
        directoryUrl = shu.createDirectoryURL(generateDirectoryName())
    }

    def "Create directory"() {
        when:
        DirectoryCreateResponse response = directoryUrl.create(null, null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 201
    }

    def "Create directory Metadata"() {
        when:
        Metadata metadata = new Metadata()
        metadata.put("key1", "value1")
        metadata.put("key2", "value2")
        DirectoryCreateResponse response = directoryUrl.create(metadata, null).blockingGet()
        DirectoryGetPropertiesResponse propResponse = directoryUrl.getProperties(null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 201
        propResponse.statusCode() == 200
        propResponse.headers().metadata() == metadata
    }

    def "Create Directory Error"() {
        setup:
        shu = su.createShareURL(generateShareName())
        directoryUrl = shu.createDirectoryURL(generateDirectoryName())

        when:
        directoryUrl.create(null, null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.SHARE_NOT_FOUND
    }

    def "Create Directory already exists"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        directoryUrl.create(null, null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 409
        e.errorCode() == StorageErrorCode.RESOURCE_ALREADY_EXISTS
    }

    def "Create Directory context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(201, DirectoryCreateHeaders)))
        directoryUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        directoryUrl.create(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Delete directory"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        DirectoryDeleteResponse response = directoryUrl.delete(null).blockingGet()

        then:
        response.headers().version() != null
        response.headers().date() != null
        response.headers().requestId() != null
        response.statusCode() == 202
    }

    def "Delete directory error"() {
        when:
        directoryUrl.delete(null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "Delete Directory context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(202, DirectoryDeleteHeaders)))
        directoryUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        directoryUrl.create(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Get properties"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        DirectoryGetPropertiesResponse response = directoryUrl.getProperties(null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 200
        response.headers().metadata().size() == 0
        // By default, data is encrypted using Microsoft Managed Keys
        response.headers().isServerEncrypted()
    }

    def "Get properties validate metadata"() {
        setup:
        Metadata metadata = new Metadata()
        metadata.put("key1", "value1")
        metadata.put("key2", "value2")
        directoryUrl.create(metadata, null).blockingGet()

        when:
        DirectoryGetPropertiesResponse response = directoryUrl.getProperties(null).blockingGet()

        then:
        validateResponseHeaders(response.headers())
        response.statusCode() == 200
        response.headers().metadata().size() == 2
        response.headers().metadata() == metadata
        // By default, data is encrypted using Microsoft Managed Keys
        response.headers().isServerEncrypted()
    }

    def "Get properties context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, DirectoryGetPropertiesHeaders)))
        directoryUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        directoryUrl.create(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Set metadata all null"() {
        when:
        directoryUrl.create(null, null).blockingGet()
        DirectorySetMetadataResponse response = directoryUrl.setMetadata(null, null).blockingGet()

        then:
        directoryUrl.getProperties(null).blockingGet().headers().metadata().size() == 0
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
        directoryUrl.create(null, null).blockingGet()

        expect:
        directoryUrl.setMetadata(metadata, null).blockingGet().statusCode() == statusCode
        directoryUrl.getProperties(null).blockingGet().headers().metadata() == metadata

        where:
        key1  | value1 | key2   | value2 || statusCode
        null  | null   | null   | null   || 200
        "foo" | "bar"  | "fizz" | "buzz" || 200
    }

    def "Set metadata error"() {
        when:
        DirectorySetMetadataResponse response = directoryUrl.setMetadata(null, null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "Set metadata context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, DirectorySetMetadataHeaders)))
        directoryUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        directoryUrl.create(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Director listFilesAndDirectories"() {
        setup:
        directoryUrl.create(null, null).blockingGet()
        def d1u = directoryUrl.createDirectoryURL(generateDirectoryName())
        d1u.create(null, null).blockingGet()

        when:
        DirectoryListFilesAndDirectoriesSegmentResponse response = directoryUrl.listFilesAndDirectoriesSegment(null, null, null, null).blockingGet()

        then:
        validateBasicHeaders(response.headers())
        response.statusCode() == 200
        response.body().entries().directoryItems().size() == 1
        response.body().entries().fileItems().size() == 0
    }

    def "Director listFilesAndDirectories marker"() {
        setup:
        directoryUrl.create(null, null).blockingGet()
        for (int i = 0; i < 10; i++){
            directoryUrl.createDirectoryURL(generateDirectoryName()).create(null, null).blockingGet()
        }

        when:
        DirectoryListFilesAndDirectoriesSegmentResponse response = directoryUrl.listFilesAndDirectoriesSegment(null, null, 6, null).blockingGet()
        String marker = response.body().nextMarker()
        int firstSegmentSize = response.body().entries().directoryItems().size() + response.body().entries().fileItems().size()
        response = directoryUrl.listFilesAndDirectoriesSegment(null, marker, null, null).blockingGet()

        then:
        validateBasicHeaders(response.headers())
        response.statusCode() == 200
        firstSegmentSize == 6
        response.body().entries().directoryItems().size() + response.body().entries().fileItems().size() == 4
    }

    def "Directory listFilesAndDirectories prefix"() {
        setup:
        directoryUrl.create(null, null).blockingGet()
        for (int i = 0; i < 10; i++){
            directoryUrl.createDirectoryURL(generateDirectoryName()).create(null, null).blockingGet()
            directoryUrl.createFileURL(generateFileName()).create(10, null, null, null).blockingGet()
        }

        when:
        DirectoryListFilesAndDirectoriesSegmentResponse response = directoryUrl.listFilesAndDirectoriesSegment(directoryPrefix, null, null, null).blockingGet()

        then:
        validateBasicHeaders(response.headers())
        response.statusCode() == 200
        response.body().entries().directoryItems().size() + response.body().entries().fileItems().size() == 10
        for (DirectoryItem directoryItem : response.body().entries().directoryItems()){
            assert directoryItem.name().startsWith(directoryPrefix)
        }
    }

    def "Directory listFilesAndDirectories maxResults"(){
        setup:
        directoryUrl.create(null, null).blockingGet()
        for (int i = 0; i < 10; i++){
            directoryUrl.createDirectoryURL(generateDirectoryName()).create(null, null).blockingGet()
            directoryUrl.createFileURL(generateFileName()).create(10, null, null, null).blockingGet()
        }

        when:
        DirectoryListFilesAndDirectoriesSegmentResponse response = directoryUrl.listFilesAndDirectoriesSegment(null, null, 10, null).blockingGet()

        then:
        validateBasicHeaders(response.headers())
        response.statusCode() == 200
        response.body().entries().directoryItems().size() + response.body().entries().fileItems().size() == 10
    }

    // We aren't able to open handles via REST, so we are only testing the 0 handles case.
    def "Directory list handles min"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        DirectoryListHandlesResponse response = directoryUrl.listHandles(null).blockingGet()

        then:
        response.statusCode() == 200
        response.body().nextMarker() == null
        response.body().handleList().size() == 0
        validateBasicHeaders(response.headers())
    }

    def "Directory list handles context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(200, DirectoryListHandlesHeaders)))
        directoryUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        directoryUrl.create(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Directory list handles"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        DirectoryListHandlesResponse response = directoryUrl.listHandles(null, null, true, Context.NONE).blockingGet()

        then:
        response.statusCode() == 200
        response.body().nextMarker() == null
        response.body().handleList().size() == 0
        validateBasicHeaders(response.headers())
    }

    def "Directory list handles error"() {
        when:
        directoryUrl.listHandles(null, defaultContext).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "Directory force close handles min"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        DirectoryForceCloseHandlesResponse response = directoryUrl.forceCloseHandles(null).blockingGet()

        then:
        response.statusCode() == 200
        response.headers().marker() == null
        response.headers().numberOfHandlesClosed() == 0
        validateBasicHeaders(response.headers())
    }

    def "Directory force close handles error"() {
        when:
        directoryUrl.forceCloseHandles(null).blockingGet()

        then:
        def e = thrown(StorageException)
        e.response().statusCode() == 404
        e.errorCode() == StorageErrorCode.RESOURCE_NOT_FOUND
    }

    def "Directory force close handles context"() {
        setup:
        def pipeline = HttpPipeline.build(getStubFactory(getContextStubPolicy(201, DirectoryForceCloseHandlesHeaders)))
        directoryUrl.withPipeline(pipeline)

        when:
        // No service call is made. Just satisfy the parameters.
        directoryUrl.create(null, defaultContext).blockingGet()

        then:
        notThrown(RuntimeException)
    }

    def "Directory force close handles"() {
        setup:
        directoryUrl.create(null, null).blockingGet()

        when:
        DirectoryForceCloseHandlesResponse response = directoryUrl.forceCloseHandles(DirectoryURL.ALL_HANDLES, null, true, Context.NONE).blockingGet()

        then:
        response.statusCode() == 200
        response.headers().marker() == null
        response.headers().numberOfHandlesClosed() == 0
        validateBasicHeaders(response.headers())
    }
}