package com.azure.storage.blob.nio;

import com.azure.storage.blob.BlobClient

import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.file.ClosedFileSystemException

class SeekableByteChannelTest extends APISpec {

    File sourceFile
    BlobClient bc
    AzureSeekableByteChannel byteChannel
    FileInputStream fileStream
    AzureFileSystem fs

    def setup() {
        sourceFile = getRandomFile(10 * 1024 * 1024)

        cc.create()
        bc = cc.getBlobClient(generateBlobName())
        bc.uploadFromFile(sourceFile.getPath())
        fs = createFS(initializeConfigMap())
        def path = ((AzurePath) fs.getPath(getNonDefaultRootDir(fs), bc.getBlobName()))

        byteChannel = new AzureSeekableByteChannel(new NioBlobInputStream(bc.openInputStream(), path))
        fileStream = new FileInputStream(sourceFile)
    }

    def "Read"() {
        // Generate buffers of random sizes and do reads until the end. Then compare arrays
    }

    def "Read fs close"() {
        when:
        fs.close()
        byteChannel.read(ByteBuffer.allocate(1))

        then:
        thrown(ClosedFileSystemException)
    }

    def "Position"() {
        setup:
        def dest = ByteBuffer.allocate(1024 * 1024)

        expect:
        byteChannel.position() == 0

        for (int i=0; i<10; i++) {
            byteChannel.read(dest)
            assert byteChannel.position() == (i+1) * 1024 * 1024
            dest.flip()
        }
    }

    def "Position fs close"() {
        when:
        fs.close()
        byteChannel.position()

        then:
        thrown(ClosedFileSystemException)
    }

    def "Size"() {
        expect:
        byteChannel.size() == 10 * 1024 * 1024

        and:
        bc.upload(defaultInputStream.get(), defaultDataSize, true)
        def path = ((AzurePath) fs.getPath(getNonDefaultRootDir(fs), bc.getBlobName()))
        byteChannel = new AzureSeekableByteChannel(new NioBlobInputStream(bc.openInputStream(), path))

        then:
        byteChannel.size() == defaultDataSize
    }

    def "Size fs closed"() {
        when:
        fs.close()
        byteChannel.size()

        then:
        thrown(ClosedFileSystemException)
    }

    def "Close"() {
        setup:
        byteChannel.close()

        when:
        byteChannel.read(ByteBuffer.allocate(1))

        then:
        thrown(ClosedChannelException)

        when:
        byteChannel.size()

        then:
        thrown(ClosedChannelException)

        when:
        byteChannel.position()

        then:
        thrown(ClosedChannelException)
    }

    def "Close fs close"() {
        when:
        fs.close()
        byteChannel.close()

        then:
        thrown(ClosedFileSystemException)
    }

    def "isOpen"() {
        expect:
        byteChannel.isOpen()

        and:
        byteChannel.close()

        then:
        !byteChannel.isOpen()
    }

    def "isOpen fs close"() {
        when:
        fs.close()
        byteChannel.isOpen()

        then:
        thrown(ClosedFileSystemException)
    }

    def "Unsupported write operations"() {
        when:
        byteChannel.write(defaultData)

        then:
        thrown(UnsupportedOperationException)

        when:
        byteChannel.truncate(0)

        then:
        thrown(UnsupportedOperationException)

        when:
        byteChannel.position(0)

        then:
        thrown(UnsupportedOperationException)
    }

    // Test in file system provider: Invalid options
    // Read
}
