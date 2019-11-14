/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute.v2019_03_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes the resource range.
 */
public class ResourceRange {
    /**
     * The minimum number of the resource.
     */
    @JsonProperty(value = "min")
    private Integer min;

    /**
     * The maximum number of the resource.
     */
    @JsonProperty(value = "max")
    private Integer max;

    /**
     * Get the minimum number of the resource.
     *
     * @return the min value
     */
    public Integer min() {
        return this.min;
    }

    /**
     * Set the minimum number of the resource.
     *
     * @param min the min value to set
     * @return the ResourceRange object itself.
     */
    public ResourceRange withMin(Integer min) {
        this.min = min;
        return this;
    }

    /**
     * Get the maximum number of the resource.
     *
     * @return the max value
     */
    public Integer max() {
        return this.max;
    }

    /**
     * Set the maximum number of the resource.
     *
     * @param max the max value to set
     * @return the ResourceRange object itself.
     */
    public ResourceRange withMax(Integer max) {
        this.max = max;
        return this;
    }

}
