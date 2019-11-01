/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.storagecache.v2019_11_01.implementation;

import com.microsoft.azure.management.storagecache.v2019_11_01.ApiOperation;
import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import com.microsoft.azure.management.storagecache.v2019_11_01.ApiOperationDisplay;

class ApiOperationImpl extends WrapperImpl<ApiOperationInner> implements ApiOperation {
    private final StorageCacheManager manager;
    ApiOperationImpl(ApiOperationInner inner, StorageCacheManager manager) {
        super(inner);
        this.manager = manager;
    }

    @Override
    public StorageCacheManager manager() {
        return this.manager;
    }

    @Override
    public ApiOperationDisplay display() {
        return this.inner().display();
    }

    @Override
    public String name() {
        return this.inner().name();
    }

}
