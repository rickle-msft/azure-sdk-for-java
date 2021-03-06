/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.containerinstance.v2018_10_01;

import rx.Completable;

/**
 * Type representing ServiceAssociationLinks.
 */
public interface ServiceAssociationLinks {
    /**
     * Delete the container instance service association link for the subnet.
     * Delete the container instance service association link for the subnet. This operation unblocks user from deleting subnet.
     *
     * @param resourceGroupName The name of the resource group.
     * @param virtualNetworkName The name of the virtual network.
     * @param subnetName The name of the subnet.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Completable deleteAsync(String resourceGroupName, String virtualNetworkName, String subnetName);

}
