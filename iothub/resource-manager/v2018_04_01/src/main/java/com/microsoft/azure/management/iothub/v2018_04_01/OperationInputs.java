/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.iothub.v2018_04_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Input values.
 */
public class OperationInputs {
    /**
     * The name of the IoT hub to check.
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    /**
     * Get the name value.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name value.
     *
     * @param name the name value to set
     * @return the OperationInputs object itself.
     */
    public OperationInputs withName(String name) {
        this.name = name;
        return this;
    }

}
