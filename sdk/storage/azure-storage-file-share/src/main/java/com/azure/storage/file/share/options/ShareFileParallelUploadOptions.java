// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.file.share.options;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.Context;
import com.azure.storage.common.ParallelTransferOptions;
import com.azure.storage.common.implementation.StorageImplUtils;
import com.azure.storage.file.share.models.ShareRequestConditions;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Map;

/**
 * Extended options that may be passed when uploading a Block Blob in parallel.
 */
@Fluent
public class ShareFileParallelUploadOptions {
    private final Flux<ByteBuffer> dataFlux;
    private final InputStream dataStream;
    private final long offset;
    private final long length;
    private ParallelTransferOptions parallelTransferOptions;
    private Duration timeout;

    /**
     * Constructs a new {@code BlobParallelUploadOptions}.
     *
     * @param dataFlux The data to write to the blob. Unlike other upload methods, this method does not require that
     * the {@code Flux} be replayable. In other words, it does not have to support multiple subscribers and is not
     * expected to produce the same values across subscriptions.
     */
    public ShareFileParallelUploadOptions(Flux<ByteBuffer> dataFlux) {
        StorageImplUtils.assertNotNull("dataFlux", dataFlux);
        this.dataFlux = dataFlux;
        this.dataStream = null;
        this.length = -1;
    }

    /**
     * Constructs a new {@code BlobParalleUploadOptions}.
     *
     * @param dataStream The data to write to the blob. The data must be markable. This is in order to support retries.
     * If the data is not markable, consider opening a {@link com.azure.storage.blob.specialized.BlobOutputStream} and
     * writing to the returned stream. Alternatively, consider wrapping your data source in a
     * {@link java.io.BufferedInputStream} to add mark support.
     * @param length The exact length of the data. It is important that this value match precisely the length of the
     * data provided in the {@link InputStream}.
     */
    public ShareFileParallelUploadOptions(InputStream dataStream, long length) {
        StorageImplUtils.assertNotNull("dataStream", length);
        StorageImplUtils.assertInBounds("length", length, 0, Long.MAX_VALUE);
        this.dataStream = dataStream;
        this.length = length;
        this.dataFlux = null;
    }

    /**
     * Gets the data source.
     *
     * @return The data to write to the blob.
     */
    public Flux<ByteBuffer> getDataFlux() {
        return this.dataFlux;
    }

    /**
     * Gets the data source.
     *
     * @return The data to write to the blob.
     */
    public InputStream getDataStream() {
        return this.dataStream;
    }

    /**
     * Gets the length of the data.
     *
     * @return The exact length of the data. It is important that this value match precisely the length of the
     * data provided in the {@link InputStream}.
     */
    public long getLength() {
        return length;
    }

    /**
     * Gets the {@link ParallelTransferOptions}.
     *
     * @return {@link ParallelTransferOptions}
     */
    public ParallelTransferOptions getParallelTransferOptions() {
        return parallelTransferOptions;
    }

    /**
     * Sets the {@link ParallelTransferOptions}.
     *
     * @param parallelTransferOptions {@link ParallelTransferOptions}
     * @return The updated options.
     */
    public ShareFileParallelUploadOptions setParallelTransferOptions(ParallelTransferOptions parallelTransferOptions) {
        this.parallelTransferOptions = parallelTransferOptions;
        return this;
    }

    /**
     * Gets the {@link BlobHttpHeaders}.
     *
     * @return {@link BlobHttpHeaders}
     */
    public BlobHttpHeaders getHeaders() {
        return headers;
    }

    /**
     * Sets the {@link BlobHttpHeaders}.
     *
     * @param headers {@link BlobHttpHeaders}
     * @return The updated options
     */
    public ShareFileParallelUploadOptions setHeaders(BlobHttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Gets the metadata.
     *
     * @return The metadata to associate with the blob.
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Sets the metadata.
     *
     * @param metadata The metadata to associate with the blob.
     * @return The updated options.
     */
    public ShareFileParallelUploadOptions setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Get the tags.
     *
     * @return The tags to associate with the blob.
     */
    public Map<String, String> getTags() {
        return tags;
    }

    /**
     * Set the tags.
     *
     * @param tags The tags to associate with the blob.
     * @return The updated options.
     */
    public ShareFileParallelUploadOptions setTags(Map<String, String> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Gets the {@link AccessTier}.
     *
     * @return {@link AccessTier}
     */
    public AccessTier getTier() {
        return tier;
    }

    /**
     * Sets the {@link AccessTier}.
     *
     * @param tier {@link AccessTier}
     * @return The updated options.
     */
    public ShareFileParallelUploadOptions setTier(AccessTier tier) {
        this.tier = tier;
        return this;
    }

    /**
     * Gets the {@link BlobRequestConditions}.
     *
     * @return {@link BlobRequestConditions}
     */
    public ShareRequestConditions getRequestConditions() {
        return requestConditions;
    }

    /**
     * Sets the {@link BlobRequestConditions}.
     *
     * @param requestConditions {@link BlobRequestConditions}
     * @return The updated options.
     */
    public ShareFileParallelUploadOptions setRequestConditions(BlobRequestConditions requestConditions) {
        this.requestConditions = requestConditions;
        return this;
    }

    /**
     * @return Whether or not the library should calculate the md5 and send it for the service to verify.
     */
    public boolean isComputeMd5() {
        return computeMd5;
    }

    /**
     * Sets the computeMd5 property.
     *
     * @param computeMd5 Whether or not the library should calculate the md5 and send it for the service to
     * verify.
     * @return The updated options.
     */
    public ShareFileParallelUploadOptions setComputeMd5(boolean computeMd5) {
        this.computeMd5 = computeMd5;
        return this;
    }

    /**
     * Gets the timeout.
     *
     * @return An optional timeout value beyond which a {@link RuntimeException} will be raised.
     *
     * @deprecated Use {@link BlobClient#uploadWithResponse(ShareFileParallelUploadOptions, Duration, Context)} to
     * specify timeout.
     */
    @Deprecated
    public Duration getTimeout() {
        return this.timeout;
    }

    /**
     * Sets the timeout.
     *
     * @param timeout An optional timeout value beyond which a {@link RuntimeException} will be raised.
     * @return The updated options
     *
     * @deprecated Use {@link BlobClient#uploadWithResponse(ShareFileParallelUploadOptions, Duration, Context)} to
     * specify timeout.
     */
    @Deprecated
    public ShareFileParallelUploadOptions setTimeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }
}
