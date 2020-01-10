// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio;

import com.azure.core.http.HttpClient;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.util.CoreUtils;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.common.policy.RequestRetryOptions;
import com.azure.storage.common.policy.RetryPolicyType;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * {@inheritDoc}
 */
public final class AzureFileSystem extends FileSystem {
    public static final String AZURE_STORAGE_ACCOUNT_KEY = "AzureStorageAccountKey";
    public static final String AZURE_STORAGE_SAS_TOKEN = "AzureStorageSasToken";
    public static final String AZURE_STORAGE_HTTP_LOG_DETAIL_LEVEL = "AzureStorageHttpLogDetailLevel";
    public static final String AZURE_STORAGE_MAX_TRIES = "AzureStorageMaxTries";
    public static final String AZURE_STORAGE_TRY_TIMEOUT = "AzureStorageTryTimeout";
    public static final String AZURE_STORAGE_RETRY_DELAY_IN_MS = "AzureStorageRetryDelayInMs";
    public static final String AZURE_STORAGE_MAX_RETRY_DELAY_IN_MS = "AzureStorageMaxRetryDelayInMs";
    public static final String AZURE_STORAGE_RETRY_POLICY_TYPE = "AzureStorageRetryPolicyType";
    public static final String AZURE_STORAGE_SECONDARY_HOST = "AzureStorageSecondaryHost";
    public static final String AZURE_STORAGE_BLOCK_SIZE = "AzureStorageBlockSize";
    public static final String AZURE_STORAGE_DOWNLOAD_RESUME_RETRIES = "AzureStorageDownloadResumeRetries";
    public static final String AZURE_STORAGE_USE_HTTP = "AzureStorageUseHttp";
    public static final String AZURE_STORAGE_FILE_STORES = "AzureStorageFileStores";
    public static final String AZURE_STORAGE_HTTP_CLIENT = "AzureStorageHttpClient"; // undocumented; for test.

    private static final String AZURE_STORAGE_ENDPOINT_TEMPLATE = "%s://%s.blob.core.windows.net";

    private final AzureFileSystemProvider parentFileSystemProvider;
    private final BlobServiceClient blobServiceClient;
    private final Integer blockSize;
    private final Integer downloadResumeRetries;
    private final Map<String, FileStore> fileStores;
    private boolean closed;

    AzureFileSystem(AzureFileSystemProvider parentFileSystemProvider, String accountName, Map<String, ?> config)
            throws IOException {
        if (Objects.isNull(parentFileSystemProvider)) {
            throw new IllegalStateException("AzureFileSystem cannot be instantiated without a parent " +
                "FileSystemProvider");
        }
        this.parentFileSystemProvider = parentFileSystemProvider;
        this.blobServiceClient = this.buildBlobServiceClient(accountName, config);
        this.blockSize = (Integer) config.get(AZURE_STORAGE_BLOCK_SIZE);
        this.downloadResumeRetries = (Integer) config.get(AZURE_STORAGE_DOWNLOAD_RESUME_RETRIES);
        try {
            this.fileStores = this.initializeFileStores(config);
        } catch (IOException e) {
            throw new IOException("Initializing FileStores failed. FileSystem could not be opened.", e);
        }
        this.closed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileSystemProvider provider() {
        return this.parentFileSystemProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.closed = true;
        this.parentFileSystemProvider.closeFileSystem(this.getFileSystemName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return !this.closed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadOnly() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSeparator() {
        return "/";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Path> getRootDirectories() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<FileStore> getFileStores() {
        return
            this.fileStores.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> supportedFileAttributeViews() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getPath(String s, String... strings) {
        return new AzurePath(this, s, strings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PathMatcher getPathMatcher(String s) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException();
    }

    String getFileSystemName() {
        return this.blobServiceClient.getAccountName();
    }

    BlobServiceClient getBlobServiceClient() {
        return this.blobServiceClient;
    }

    private BlobServiceClient buildBlobServiceClient(String accountName, Map<String,?> config) {
        String scheme = config.containsKey(AZURE_STORAGE_USE_HTTP)
                && (Boolean) config.get(AZURE_STORAGE_USE_HTTP)
                ? "http" : "https";
        BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
                .endpoint(String.format(AZURE_STORAGE_ENDPOINT_TEMPLATE, scheme, accountName));

        if (config.containsKey(AZURE_STORAGE_ACCOUNT_KEY)) {
            builder.credential(new StorageSharedKeyCredential(accountName,
                    (String)config.get(AZURE_STORAGE_ACCOUNT_KEY)));
        }
        else if (config.containsKey(AZURE_STORAGE_SAS_TOKEN)) {
            builder.sasToken((String) config.get(AZURE_STORAGE_SAS_TOKEN));
        }
        else {
            throw new IllegalArgumentException(String.format("No credentials were provided. Please specify one of the" +
                    " following when constructing an AzureFileSystem: %s, %s.", AZURE_STORAGE_ACCOUNT_KEY,
                    AZURE_STORAGE_SAS_TOKEN));
        }

        builder.httpLogOptions(new HttpLogOptions()
            .setLogLevel((HttpLogDetailLevel)config.get(AZURE_STORAGE_HTTP_LOG_DETAIL_LEVEL)));

        RequestRetryOptions retryOptions = new RequestRetryOptions(
            (RetryPolicyType)config.get(AZURE_STORAGE_RETRY_POLICY_TYPE),
            (Integer)config.get(AZURE_STORAGE_MAX_TRIES),
            (Integer)config.get(AZURE_STORAGE_TRY_TIMEOUT),
            (Long)config.get(AZURE_STORAGE_RETRY_DELAY_IN_MS),
            (Long)config.get(AZURE_STORAGE_MAX_RETRY_DELAY_IN_MS),
            (String)config.get(AZURE_STORAGE_SECONDARY_HOST));
        builder.retryOptions(retryOptions);

        builder.httpClient((HttpClient)config.get(AZURE_STORAGE_HTTP_CLIENT));

        return builder.buildClient();
    }

    private Map<String, FileStore> initializeFileStores(Map<String, ?> config) throws IOException {
        String fileStoreNames = (String)config.get(AZURE_STORAGE_FILE_STORES);
        if (CoreUtils.isNullOrEmpty(fileStoreNames)) {
            throw new IllegalArgumentException("The list of FileStores cannot be null.");
        }

        Map<String, FileStore> fileStores = new HashMap<>();
        for (String fileStoreName : fileStoreNames.split(",")) {
            fileStores.put(fileStoreName, new AzureFileStore(this, fileStoreName));
        }

        return fileStores;
    }
}
