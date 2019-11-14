/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.sql.v2017_03_01_preview;

import com.microsoft.azure.arm.collection.SupportsCreating;
import rx.Observable;
import com.microsoft.azure.management.sql.v2017_03_01_preview.implementation.ManagedDatabaseSecurityAlertPoliciesInner;
import com.microsoft.azure.arm.model.HasInner;

/**
 * Type representing ManagedDatabaseSecurityAlertPolicies.
 */
public interface ManagedDatabaseSecurityAlertPolicies extends SupportsCreating<ManagedDatabaseSecurityAlertPolicy.DefinitionStages.Blank>, HasInner<ManagedDatabaseSecurityAlertPoliciesInner> {
    /**
     * Gets a managed database's security alert policy.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param managedInstanceName The name of the managed instance.
     * @param databaseName The name of the managed database for which the security alert policy is defined.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<ManagedDatabaseSecurityAlertPolicy> getAsync(String resourceGroupName, String managedInstanceName, String databaseName);

    /**
     * Gets a list of managed database's security alert policies.
     *
     * @param resourceGroupName The name of the resource group that contains the resource. You can obtain this value from the Azure Resource Manager API or the portal.
     * @param managedInstanceName The name of the managed instance.
     * @param databaseName The name of the managed database for which the security alert policies are defined.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<ManagedDatabaseSecurityAlertPolicy> listByDatabaseAsync(final String resourceGroupName, final String managedInstanceName, final String databaseName);

}
