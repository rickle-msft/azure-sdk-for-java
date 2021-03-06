// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.common.implementation.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * Utility type exposing methods to deal with {@link Flux}.
 */
public final class FluxUtil {
    /**
     * Checks if a type is Flux&lt;ByteBuf&gt;.
     *
     * @param entityType the type to check
     * @return whether the type represents a Flux that emits ByteBuf
     */
    public static boolean isFluxByteBuf(Type entityType) {
        if (TypeUtil.isTypeOrSubTypeOf(entityType, Flux.class)) {
            final Type innerType = TypeUtil.getTypeArguments(entityType)[0];
            if (TypeUtil.isTypeOrSubTypeOf(innerType, ByteBuf.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Collects ByteBuf emitted by a Flux into a byte array.
     * @param stream A stream which emits ByteBuf instances.
     * @param autoReleaseEnabled if ByteBuf instances in stream gets automatically released as they consumed
     * @return A Mono which emits the concatenation of all the ByteBuf instances given by the source Flux.
     */
    public static Mono<byte[]> collectBytesInByteBufStream(Flux<ByteBuf> stream, boolean autoReleaseEnabled) {
        if (autoReleaseEnabled) {
            // A stream is auto-release enabled means - the ByteBuf chunks in the stream get
            // released as consumer consumes each chunk.
            return Mono.using(Unpooled::compositeBuffer,
                cbb -> stream.collect(() -> cbb,
                    (cbb1, buffer) -> cbb1.addComponent(true, Unpooled.wrappedBuffer(buffer).retain())),
                    ReferenceCountUtil::release)
                    .filter((CompositeByteBuf cbb) -> cbb.isReadable())
                    .map(FluxUtil::byteBufToArray);
        } else {
            return stream.collect(Unpooled::compositeBuffer,
                (cbb1, buffer) -> cbb1.addComponent(true, Unpooled.wrappedBuffer(buffer)))
                    .filter((CompositeByteBuf cbb) -> cbb.isReadable())
                    .map(FluxUtil::byteBufToArray);
        }
    }

    /**
     * Splits a ByteBuf into ByteBuf chunks.
     *
     * @param whole the ByteBuf to split
     * @param chunkSize the maximum size of each ByteBuf chunk
     * @return A stream that emits chunks of the original whole ByteBuf
     */
    public static Flux<ByteBuf> split(final ByteBuf whole, final int chunkSize) {
        return Flux.generate(whole::readerIndex, (readFromIndex, synchronousSync) -> {
            final int writerIndex = whole.writerIndex();
            //
            if (readFromIndex >= writerIndex) {
                synchronousSync.complete();
                return writerIndex;
            } else {
                int readSize = Math.min(writerIndex - readFromIndex, chunkSize);
                // Netty slice operation will not increment the ref count.
                //
                // Here we invoke 'retain' on each slice, since
                // consumer of the returned Flux stream is responsible for
                // releasing each chunk as it gets consumed.
                //
                synchronousSync.next(whole.slice(readFromIndex, readSize).retain());
                return readFromIndex + readSize;
            }
        });
    }

    /**
     * Gets the content of the provided ByteBuf as a byte array.
     * This method will create a new byte array even if the ByteBuf can
     * have optionally backing array.
     *
     *
     * @param byteBuf the byte buffer
     * @return the byte array
     */
    public static byte[] byteBufToArray(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        byte[] byteArray = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), byteArray);
        return byteArray;
    }

    /**
     * Collects byte buffers emitted by a Flux into a ByteBuf.
     *
     * @param stream A stream which emits ByteBuf instances.
     * @param autoReleaseEnabled if ByteBuf instances in stream gets automatically released as they consumed
     * @return A Mono which emits the concatenation of all the byte buffers given by the source Flux.
     */
    public static Mono<ByteBuf> collectByteBufStream(Flux<ByteBuf> stream, boolean autoReleaseEnabled) {
        if (autoReleaseEnabled) {
            Mono<ByteBuf> mergedCbb = Mono.using(
                    // Resource supplier
                () -> {
                    CompositeByteBuf initialCbb = Unpooled.compositeBuffer();
                    return initialCbb;
                },
                    // source Mono creator
                (CompositeByteBuf initialCbb) -> {
                    Mono<CompositeByteBuf> reducedCbb = stream.reduce(initialCbb, (CompositeByteBuf currentCbb, ByteBuf nextBb) -> {
                        CompositeByteBuf updatedCbb = currentCbb.addComponent(nextBb.retain());
                        return updatedCbb;
                    });
                    //
                    return reducedCbb
                            .doOnNext((CompositeByteBuf cbb) -> cbb.writerIndex(cbb.capacity()))
                            .filter((CompositeByteBuf cbb) -> cbb.isReadable());
                },
                    // Resource cleaner
                (CompositeByteBuf finalCbb) -> finalCbb.release());
            return mergedCbb;
        } else {
            return stream.collect(Unpooled::compositeBuffer,
                (cbb1, buffer) -> cbb1.addComponent(true, Unpooled.wrappedBuffer(buffer)))
                    .filter((CompositeByteBuf cbb) -> cbb.isReadable())
                    .map(bb -> bb);
        }
    }

    private static final int DEFAULT_CHUNK_SIZE = 1024 * 64;

    //region Utility methods to create Flux<ByteBuf> that read and emits chunks from AsynchronousFileChannel.

    /**
     * Creates a {@link Flux} from an {@link AsynchronousFileChannel}
     * which reads part of a file into chunks of the given size.
     *
     * @param fileChannel The file channel.
     * @param chunkSize the size of file chunks to read.
     * @param offset The offset in the file to begin reading.
     * @param length The number of bytes to read from the file.
     * @return the Flowable.
     */
    public static Flux<ByteBuf> byteBufStreamFromFile(AsynchronousFileChannel fileChannel, int chunkSize, long offset, long length) {
        return new ByteBufStreamFromFile(fileChannel, chunkSize, offset, length);
    }

    /**
     * Creates a {@link Flux} from an {@link AsynchronousFileChannel}
     * which reads part of a file.
     *
     * @param fileChannel The file channel.
     * @param offset The offset in the file to begin reading.
     * @param length The number of bytes to read from the file.
     * @return the Flowable.
     */
    public static Flux<ByteBuf> byteBufStreamFromFile(AsynchronousFileChannel fileChannel, long offset, long length) {
        return byteBufStreamFromFile(fileChannel, DEFAULT_CHUNK_SIZE, offset, length);
    }

    /**
     * Creates a {@link Flux} from an {@link AsynchronousFileChannel}
     * which reads the entire file.
     *
     * @param fileChannel The file channel.
     * @return The AsyncInputStream.
     */
    public static Flux<ByteBuf> byteBufStreamFromFile(AsynchronousFileChannel fileChannel) {
        try {
            long size = fileChannel.size();
            return byteBufStreamFromFile(fileChannel, DEFAULT_CHUNK_SIZE, 0, size);
        } catch (IOException e) {
            return Flux.error(e);
        }
    }
    //endregion

    //region ByteBufStreamFromFile implementation
    private static final class ByteBufStreamFromFile extends Flux<ByteBuf> {
        private final ByteBufAllocator alloc;
        private final AsynchronousFileChannel fileChannel;
        private final int chunkSize;
        private final long offset;
        private final long length;

        ByteBufStreamFromFile(AsynchronousFileChannel fileChannel, int chunkSize, long offset, long length) {
            this.alloc = ByteBufAllocator.DEFAULT;
            this.fileChannel = fileChannel;
            this.chunkSize = chunkSize;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void subscribe(CoreSubscriber<? super ByteBuf> actual) {
            FileReadSubscription subscription = new FileReadSubscription(actual, fileChannel, alloc, chunkSize, offset, length);
            actual.onSubscribe(subscription);
        }

        static final class FileReadSubscription implements Subscription, CompletionHandler<Integer, ByteBuf> {
            private static final int NOT_SET = -1;
            private static final long serialVersionUID = -6831808726875304256L;
            //
            private final Subscriber<? super ByteBuf> subscriber;
            private volatile long position;
            //
            private final AsynchronousFileChannel fileChannel;
            private final ByteBufAllocator alloc;
            private final int chunkSize;
            private final long offset;
            private final long length;
            //
            private volatile boolean done;
            private Throwable error;
            private volatile ByteBuf next;
            private volatile boolean cancelled;
            //
            volatile int wip;
            @SuppressWarnings("rawtypes")
            static final AtomicIntegerFieldUpdater<FileReadSubscription> WIP = AtomicIntegerFieldUpdater.newUpdater(FileReadSubscription.class, "wip");
            volatile long requested;
            @SuppressWarnings("rawtypes")
            static final AtomicLongFieldUpdater<FileReadSubscription> REQUESTED = AtomicLongFieldUpdater.newUpdater(FileReadSubscription.class, "requested");
            //

            FileReadSubscription(Subscriber<? super ByteBuf> subscriber, AsynchronousFileChannel fileChannel, ByteBufAllocator alloc, int chunkSize, long offset, long length) {
                this.subscriber = subscriber;
                //
                this.fileChannel = fileChannel;
                this.alloc = alloc;
                this.chunkSize = chunkSize;
                this.offset = offset;
                this.length = length;
                //
                this.position = NOT_SET;
            }

            //region Subscription implementation

            @Override
            public void request(long n) {
                if (Operators.validate(n)) {
                    Operators.addCap(REQUESTED, this, n);
                    drain();
                }
            }

            @Override
            public void cancel() {
                this.cancelled = true;
            }

            //endregion

            //region CompletionHandler implementation

            @Override
            public void completed(Integer bytesRead, ByteBuf buffer) {
                if (!cancelled) {
                    if (bytesRead == -1) {
                        done = true;
                    } else {
                        // use local variable to perform fewer volatile reads
                        long pos = position;
                        //
                        int bytesWanted = (int) Math.min(bytesRead, maxRequired(pos));
                        buffer.writerIndex(bytesWanted);
                        long position2 = pos + bytesWanted;
                        //noinspection NonAtomicOperationOnVolatileField
                        position = position2;
                        next = buffer;
                        if (position2 >= offset + length) {
                            done = true;
                        }
                    }
                    drain();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuf attachment) {
                if (!cancelled) {
                    // must set error before setting done to true
                    // so that is visible in drain loop
                    error = exc;
                    done = true;
                    drain();
                }
            }

            //endregion

            private void drain() {
                if (WIP.getAndIncrement(this) != 0) {
                    return;
                }
                // on first drain (first request) we initiate the first read
                if (position == NOT_SET) {
                    position = offset;
                    doRead();
                }
                int missed = 1;
                for (;;) {
                    if (cancelled) {
                        return;
                    }
                    if (REQUESTED.get(this) > 0) {
                        boolean emitted = false;
                        // read d before next to avoid race
                        boolean d = done;
                        ByteBuf bb = next;
                        if (bb != null) {
                            next = null;
                            //
                            // try {
                            subscriber.onNext(bb);
                            // } finally {
                                // Note: Don't release here, we follow netty disposal pattern
                                // it's consumers responsiblity to release chunks after consumption.
                                //
                                // ReferenceCountUtil.release(bb);
                            // }
                            //
                            emitted = true;
                        } else {
                            emitted = false;
                        }
                        if (d) {
                            if (error != null) {
                                subscriber.onError(error);
                                // exit without reducing wip so that further drains will be NOOP
                                return;
                            } else {
                                subscriber.onComplete();
                                // exit without reducing wip so that further drains will be NOOP
                                return;
                            }
                        }
                        if (emitted) {
                            // do this after checking d to avoid calling read
                            // when done
                            Operators.produced(REQUESTED, this, 1);
                            //
                            doRead();
                        }
                    }
                    missed = WIP.addAndGet(this, -missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }

            private void doRead() {
                // use local variable to limit volatile reads
                long pos = position;
                int readSize = Math.min(chunkSize, maxRequired(pos));
                ByteBuf innerBuf = alloc.buffer(readSize, readSize);
                fileChannel.read(innerBuf.nioBuffer(0, readSize), pos, innerBuf, this);
            }

            private int maxRequired(long pos) {
                long maxRequired = offset + length - pos;
                if (maxRequired <= 0) {
                    return 0;
                } else {
                    int m = (int) (maxRequired);
                    // support really large files by checking for overflow
                    if (m < 0) {
                        return Integer.MAX_VALUE;
                    } else {
                        return m;
                    }
                }
            }
        }
    }

    //endregion

    // Private Ctr
    private FluxUtil() {
    }
}
