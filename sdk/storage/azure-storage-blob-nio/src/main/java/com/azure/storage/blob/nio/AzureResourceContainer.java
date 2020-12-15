// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio;

import com.azure.core.util.logging.ClientLogger;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobRequestConditions;

import java.io.IOException;
import java.util.Collections;

public class AzureResourceContainer extends AzureResource {
    private final ClientLogger logger = new ClientLogger(AzureResourceContainer.class);

    private final BlobContainerClient containerClient;

    AzureResourceContainer(AzurePath path) throws IOException {
        super(path);
        validateRoot();
        this.containerClient = path.toContainerClient();
    }

    @Override
    boolean checkParentDirectoryExists() {
        throw LoggingUtility.logError(logger,
            new UnsupportedOperationException("Operations which require validating parent existence such as copy and " +
                "create are not supported on root directories."));
    }

    /**
     * Root containers are always assumed to exist as they are validated on startup and cannot be deleted through the
     * file system. No need to make a service call.
     */
    @Override
    boolean checkDirectoryExists() throws IOException {
        return true;
    }

    @Override
    DirectoryStatus checkDirStatus() throws IOException {
        throw LoggingUtility.logError(logger,
            new UnsupportedOperationException("Operations which require checking directory status such as copy and " +
                "delete are not supported on root directories."));
    }

    @Override
    void putDirectoryBlob(BlobRequestConditions requestConditions) {
        throw LoggingUtility.logError(logger,
            new UnsupportedOperationException("Operations which require checking directory status such as copy and " +
                "delete are not supported on root directories."));
    }

    private void validateRoot() {
        if (!this.path.isRoot()) {
            throw LoggingUtility.logError(logger, new IllegalArgumentException(
                "Path must be a root. Path: " + this.path.toString()));
        }
    }
}
