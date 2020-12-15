// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio;

import com.azure.core.util.logging.ClientLogger;
import com.azure.storage.blob.BlobContainerClient;

import java.io.IOException;

public class AzureResourceContainer extends AzureResource {
    private final ClientLogger logger = new ClientLogger(AzureResourceContainer.class);

    private final BlobContainerClient containerClient;

    AzureResourceContainer(AzurePath path) throws IOException {
        super(path);
        validateRoot();
        this.containerClient = path.toContainerClient();
    }

    /**
     * Checks for the existence of the parent of the given path. We do not check for the actual marker blob as parents
     * need only weakly exist.
     *
     * If the parent is a root (container), it will be assumed to exist, so it must be validated elsewhere that the
     * container is a legitimate root within this file system.
     */
    @Override
    boolean checkParentDirectoryExists() throws IOException {
        return true;
    }

    private void validateRoot() {
        if (!this.path.isRoot()) {
            throw LoggingUtility.logError(logger, new IllegalArgumentException(
                "Path must be a root. Path: " + this.path.toString()));
        }
    }
}
