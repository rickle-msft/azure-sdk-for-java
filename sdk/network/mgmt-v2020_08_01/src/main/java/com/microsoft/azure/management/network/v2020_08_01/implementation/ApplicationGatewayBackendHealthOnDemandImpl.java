/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network.v2020_08_01.implementation;

import com.microsoft.azure.management.network.v2020_08_01.ApplicationGatewayBackendHealthOnDemand;
import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import com.microsoft.azure.management.network.v2020_08_01.ApplicationGatewayBackendAddressPool;
import com.microsoft.azure.management.network.v2020_08_01.ApplicationGatewayBackendHealthHttpSettings;

class ApplicationGatewayBackendHealthOnDemandImpl extends WrapperImpl<ApplicationGatewayBackendHealthOnDemandInner> implements ApplicationGatewayBackendHealthOnDemand {
    private final NetworkManager manager;
    ApplicationGatewayBackendHealthOnDemandImpl(ApplicationGatewayBackendHealthOnDemandInner inner, NetworkManager manager) {
        super(inner);
        this.manager = manager;
    }

    @Override
    public NetworkManager manager() {
        return this.manager;
    }

    @Override
    public ApplicationGatewayBackendAddressPool backendAddressPool() {
        return this.inner().backendAddressPool();
    }

    @Override
    public ApplicationGatewayBackendHealthHttpSettings backendHealthHttpSettings() {
        return this.inner().backendHealthHttpSettings();
    }

}
