// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.common;

    /*
     * Gets the SDK information for each library component.
     */
public class ServiceConfiguration {
    public class QueueConfiguration {
        //TODO: Eventually remove these hardcoded strings with https://github.com/Azure/azure-sdk-for-java/issues/3141
        public static final String NAME = "azure-storage-queue";
        public static final String VERSION = "12.0.0-preview.3";
    }

    public class BlobConfiguration {
        public static final String NAME = "azure-storage-blob";
        public static final String VERSION = "12.0.0-preview.3";
    }

    public class FileConfiguration {
        //TODO: Eventually remove these hardcoded strings with https://github.com/Azure/azure-sdk-for-java/issues/3141
        public static final String NAME = "azure-storage-file";
        public static final String VERSION = "12.0.0-preview.3";
    }
}
