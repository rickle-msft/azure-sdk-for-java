/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.appplatform.v2020_11_01_preview;

import com.microsoft.azure.arm.collection.SupportsCreating;
import rx.Completable;
import rx.Observable;
import com.microsoft.azure.management.appplatform.v2020_11_01_preview.implementation.AppsInner;
import com.microsoft.azure.arm.model.HasInner;

/**
 * Type representing Apps.
 */
public interface Apps extends SupportsCreating<AppResource.DefinitionStages.Blank>, HasInner<AppsInner> {
    /**
     * Get an App and its properties.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serviceName The name of the Service resource.
     * @param appName The name of the App resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<AppResource> getAsync(String resourceGroupName, String serviceName, String appName);

    /**
     * Operation to delete an App.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serviceName The name of the Service resource.
     * @param appName The name of the App resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Completable deleteAsync(String resourceGroupName, String serviceName, String appName);

    /**
     * Handles requests to list all resources in a Service.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serviceName The name of the Service resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<AppResource> listAsync(final String resourceGroupName, final String serviceName);

    /**
     * Get an resource upload URL for an App, which may be artifacts or source archive.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serviceName The name of the Service resource.
     * @param appName The name of the App resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<ResourceUploadDefinition> getResourceUploadUrlAsync(String resourceGroupName, String serviceName, String appName);

    /**
     * Check the resource name is valid as well as not in use.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param serviceName The name of the Service resource.
     * @param appName The name of the App resource.
     * @param name Name to be validated
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<CustomDomainValidateResult> validateDomainAsync(String resourceGroupName, String serviceName, String appName, String name);

}
