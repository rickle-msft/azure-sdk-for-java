/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.mediaservices.v2019_05_01_preview;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The input to the check name availability request.
 */
public class CheckNameAvailabilityInput {
    /**
     * The account name.
     */
    @JsonProperty(value = "name")
    private String name;

    /**
     * The account type. For a Media Services account, this should be
     * 'MediaServices'.
     */
    @JsonProperty(value = "type")
    private String type;

    /**
     * Get the account name.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the account name.
     *
     * @param name the name value to set
     * @return the CheckNameAvailabilityInput object itself.
     */
    public CheckNameAvailabilityInput withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the account type. For a Media Services account, this should be 'MediaServices'.
     *
     * @return the type value
     */
    public String type() {
        return this.type;
    }

    /**
     * Set the account type. For a Media Services account, this should be 'MediaServices'.
     *
     * @param type the type value to set
     * @return the CheckNameAvailabilityInput object itself.
     */
    public CheckNameAvailabilityInput withType(String type) {
        this.type = type;
        return this;
    }

}
