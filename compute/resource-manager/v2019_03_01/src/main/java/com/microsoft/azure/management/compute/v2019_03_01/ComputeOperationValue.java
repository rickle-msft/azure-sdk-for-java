/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute.v2019_03_01;

import com.microsoft.azure.arm.model.HasInner;
import com.microsoft.azure.arm.resources.models.HasManager;
import com.microsoft.azure.management.compute.v2019_03_01.implementation.ComputeManager;
import com.microsoft.azure.management.compute.v2019_03_01.implementation.ComputeOperationValueInner;

/**
 * Type representing ComputeOperationValue.
 */
public interface ComputeOperationValue extends HasInner<ComputeOperationValueInner>, HasManager<ComputeManager> {
    /**
     * @return the description value.
     */
    String description();

    /**
     * @return the name value.
     */
    String name();

    /**
     * @return the operation value.
     */
    String operation();

    /**
     * @return the origin value.
     */
    String origin();

    /**
     * @return the provider value.
     */
    String provider();

    /**
     * @return the resource value.
     */
    String resource();

}
