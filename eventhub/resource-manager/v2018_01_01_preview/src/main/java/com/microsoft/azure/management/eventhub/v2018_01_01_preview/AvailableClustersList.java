/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.eventhub.v2018_01_01_preview;

import com.microsoft.azure.arm.model.HasInner;
import com.microsoft.azure.arm.resources.models.HasManager;
import com.microsoft.azure.management.eventhub.v2018_01_01_preview.implementation.EventHubManager;
import com.microsoft.azure.management.eventhub.v2018_01_01_preview.implementation.AvailableClustersListInner;
import java.util.Map;

/**
 * Type representing AvailableClustersList.
 */
public interface AvailableClustersList extends HasInner<AvailableClustersListInner>, HasManager<EventHubManager> {
    /**
     * @return the availableClusters value.
     */
    Map<String, Integer> availableClusters();

}
