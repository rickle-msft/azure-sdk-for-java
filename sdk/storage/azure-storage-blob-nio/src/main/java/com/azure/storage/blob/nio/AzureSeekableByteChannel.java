// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio;

import com.azure.core.util.logging.ClientLogger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A byte channel that maintains a current position.
 * <p>
 * This type is primarily offered to support some jdk convenience methods such as
 * {@link Files#createFile(Path, FileAttribute[])} which requires opening a channel and closing it. A very limited set
 * of functionality is offered here. More specifically, only reads--no writes or seeks--are supported. This type is not
 * threadsafe.
 * <p>
 * {@link NioBlobInputStream} and {@link NioBlobOutputStream} are the preferred types for reading and writing blob data.
 */
public class AzureSeekableByteChannel implements SeekableByteChannel {
    private final ClientLogger logger = new ClientLogger(AzureSeekableByteChannel.class);

    private final NioBlobInputStream inputStream;
    private long position; // Needs to be threadsafe?
    private boolean closed = false;

    AzureSeekableByteChannel(NioBlobInputStream inputStream) {
        this.inputStream = inputStream;
        inputStream.mark(Integer.MAX_VALUE);
        this.position = 0; // Assumes the stream is always initialized to point at the beginning of the blob
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        validateOpen();
        int count = 0;

        int len = dst.remaining();
        byte[] buf = new byte[len];

        while (count < len) {
            int retCount = this.inputStream.read(buf, count, len - count);
            if (retCount == -1) {
                break;
            }
            count += retCount;
        }
        dst.put(buf, 0, count);
        this.position += count;

        return count;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        throw logger.logThrowableAsError(new UnsupportedOperationException());
    }

    @Override
    public long position() throws IOException {
        validateOpen();
        return position;
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
        // We could possibly support position for reading if we always mark at the beginning and then reset and skip
        // forward for a seek.
        // Not necessary for now, though.
        throw logger.logThrowableAsError(new UnsupportedOperationException());
    }

    @Override
    public long size() throws IOException {
        validateOpen();
        return inputStream.getBlobInputStream().getProperties().getBlobSize();
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        throw logger.logThrowableAsError(new UnsupportedOperationException());
    }

    @Override
    public boolean isOpen() {
        return !this.closed;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        this.closed = true;
    }

    private void validateOpen() throws ClosedChannelException {
        if (this.closed) {
            throw LoggingUtility.logError(logger, new ClosedChannelException());
        }
    }
}
