// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.authorization.fluent.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

/** insightIdentity. */
@Fluent
public class MicrosoftGraphInsightIdentity {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(MicrosoftGraphInsightIdentity.class);

    /*
     * The email address of the user who shared the item.
     */
    @JsonProperty(value = "address")
    private String address;

    /*
     * The display name of the user who shared the item.
     */
    @JsonProperty(value = "displayName")
    private String displayName;

    /*
     * The id of the user who shared the item.
     */
    @JsonProperty(value = "id")
    private String id;

    /*
     * insightIdentity
     */
    @JsonIgnore private Map<String, Object> additionalProperties;

    /**
     * Get the address property: The email address of the user who shared the item.
     *
     * @return the address value.
     */
    public String address() {
        return this.address;
    }

    /**
     * Set the address property: The email address of the user who shared the item.
     *
     * @param address the address value to set.
     * @return the MicrosoftGraphInsightIdentity object itself.
     */
    public MicrosoftGraphInsightIdentity withAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Get the displayName property: The display name of the user who shared the item.
     *
     * @return the displayName value.
     */
    public String displayName() {
        return this.displayName;
    }

    /**
     * Set the displayName property: The display name of the user who shared the item.
     *
     * @param displayName the displayName value to set.
     * @return the MicrosoftGraphInsightIdentity object itself.
     */
    public MicrosoftGraphInsightIdentity withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Get the id property: The id of the user who shared the item.
     *
     * @return the id value.
     */
    public String id() {
        return this.id;
    }

    /**
     * Set the id property: The id of the user who shared the item.
     *
     * @param id the id value to set.
     * @return the MicrosoftGraphInsightIdentity object itself.
     */
    public MicrosoftGraphInsightIdentity withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get the additionalProperties property: insightIdentity.
     *
     * @return the additionalProperties value.
     */
    @JsonAnyGetter
    public Map<String, Object> additionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Set the additionalProperties property: insightIdentity.
     *
     * @param additionalProperties the additionalProperties value to set.
     * @return the MicrosoftGraphInsightIdentity object itself.
     */
    public MicrosoftGraphInsightIdentity withAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    @JsonAnySetter
    void withAdditionalProperties(String key, Object value) {
        if (additionalProperties == null) {
            additionalProperties = new HashMap<>();
        }
        additionalProperties.put(key, value);
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
    }
}
