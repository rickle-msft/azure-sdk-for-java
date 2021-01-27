// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.kusto.implementation;

import com.azure.resourcemanager.kusto.KustoManager;
import com.azure.resourcemanager.kusto.fluent.models.AzureResourceSkuInner;
import com.azure.resourcemanager.kusto.models.AzureCapacity;
import com.azure.resourcemanager.kusto.models.AzureResourceSku;
import com.azure.resourcemanager.kusto.models.AzureSku;

public final class AzureResourceSkuImpl implements AzureResourceSku {
    private AzureResourceSkuInner innerObject;

    private final KustoManager serviceManager;

    AzureResourceSkuImpl(AzureResourceSkuInner innerObject, KustoManager serviceManager) {
        this.innerObject = innerObject;
        this.serviceManager = serviceManager;
    }

    public String resourceType() {
        return this.innerModel().resourceType();
    }

    public AzureSku sku() {
        return this.innerModel().sku();
    }

    public AzureCapacity capacity() {
        return this.innerModel().capacity();
    }

    public AzureResourceSkuInner innerModel() {
        return this.innerObject;
    }

    private KustoManager manager() {
        return this.serviceManager;
    }
}
