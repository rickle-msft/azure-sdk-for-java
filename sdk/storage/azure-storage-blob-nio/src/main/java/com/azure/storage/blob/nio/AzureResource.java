// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio;

import com.azure.core.util.logging.ClientLogger;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.common.implementation.Constants;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This type is meant to be a logical grouping of operations and data associated with an azure resource. It is NOT
 * intended to serve as a local cache for any data related to remote resources. It is agnostic to whether the resource
 * is a directory or a file and will not perform any validation of the resource type.
 *
 * It also serves as the interface to Storage clients. Any operation that needs to use a client should first build an
 * AzureResource using a path and then use the getter to access the client.
 */
abstract class AzureResource {
    private final ClientLogger logger = new ClientLogger(AzureResource.class);

    static final String DIR_METADATA_MARKER = Constants.HeaderConstants.DIRECTORY_METADATA_KEY;

    protected final AzurePath path;

    public static AzureResource newAzureResource(Path path, ClientLogger logger) throws IOException {
        AzurePath aPath = validatePathInstanceType(path, logger);
        if (aPath.isRoot()) {
            return new AzureResourceContainer(aPath);
        } else {
            return new AzureResourceBlob(aPath);
        }
    }

    AzureResource(AzurePath path) {
        Objects.requireNonNull(path, "path");
        this.path = path;
    }

    /**
     * Checks for the existence of the parent of the given path. We do not check for the actual marker blob as parents
     * need only weakly exist.
     *
     * If the parent is a root (container), it will be assumed to exist, so it must be validated elsewhere that the
     * container is a legitimate root within this file system.
     *
     * If the given path is a root, its parent is also assumed to exist for convenience, even though the request is
     * somewhat nonsensical.
     */
    abstract boolean checkParentDirectoryExists() throws IOException;

    /**
     * Checks whether a directory exists by either being empty or having children.
     */
    abstract boolean checkDirectoryExists() throws IOException;

    /**
     * This method will check if a directory is extant and/or empty and accommodates virtual directories. This method
     * will not check the status of root directories.
     */
    abstract DirectoryStatus checkDirStatus() throws IOException;

    /**
     * Creates the actual directory marker. This method should only be used when any necessary checks for proper
     * conditions of directory creation (e.g. parent existence) have already been performed. Otherwise,
     * {@link AzureFileSystemProvider#createDirectory(Path, FileAttribute[])} should be preferred.
     *
     * @param requestConditions Any necessary request conditions to pass when creating the directory blob.
     */
    abstract void putDirectoryBlob(BlobRequestConditions requestConditions);

    private static AzurePath validatePathInstanceType(Path path, ClientLogger logger) {
        if (!(path instanceof AzurePath)) {
            throw LoggingUtility.logError(logger, new IllegalArgumentException("This provider cannot operate on "
                + "subtypes of Path other than AzurePath"));
        }
        return (AzurePath) path;
    }
}
