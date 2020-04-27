// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.storage.blob.implementation.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The QuickQueryFormat model.
 */
@JacksonXmlRootElement(localName = "QuickQueryFormat")
@Fluent
public final class QuickQueryFormat {
    /*
     * Possible values include: 'delimited', 'json'
     */
    @JsonProperty(value = "Type")
    private QuickQueryFormatType type;

    /*
     * The delimitedTextConfiguration property.
     */
    @JsonProperty(value = "DelimitedTextConfiguration")
    private DelimitedTextConfiguration delimitedTextConfiguration;

    /*
     * The jsonTextConfiguration property.
     */
    @JsonProperty(value = "JsonTextConfiguration")
    private JsonTextConfiguration jsonTextConfiguration;

    /**
     * Get the type property: Possible values include: 'delimited', 'json'.
     *
     * @return the type value.
     */
    public QuickQueryFormatType getType() {
        return this.type;
    }

    /**
     * Set the type property: Possible values include: 'delimited', 'json'.
     *
     * @param type the type value to set.
     * @return the QuickQueryFormat object itself.
     */
    public QuickQueryFormat setType(QuickQueryFormatType type) {
        this.type = type;
        return this;
    }

    /**
     * Get the delimitedTextConfiguration property: The
     * delimitedTextConfiguration property.
     *
     * @return the delimitedTextConfiguration value.
     */
    public DelimitedTextConfiguration getDelimitedTextConfiguration() {
        return this.delimitedTextConfiguration;
    }

    /**
     * Set the delimitedTextConfiguration property: The
     * delimitedTextConfiguration property.
     *
     * @param delimitedTextConfiguration the delimitedTextConfiguration value
     * to set.
     * @return the QuickQueryFormat object itself.
     */
    public QuickQueryFormat setDelimitedTextConfiguration(DelimitedTextConfiguration delimitedTextConfiguration) {
        this.delimitedTextConfiguration = delimitedTextConfiguration;
        return this;
    }

    /**
     * Get the jsonTextConfiguration property: The jsonTextConfiguration
     * property.
     *
     * @return the jsonTextConfiguration value.
     */
    public JsonTextConfiguration getJsonTextConfiguration() {
        return this.jsonTextConfiguration;
    }

    /**
     * Set the jsonTextConfiguration property: The jsonTextConfiguration
     * property.
     *
     * @param jsonTextConfiguration the jsonTextConfiguration value to set.
     * @return the QuickQueryFormat object itself.
     */
    public QuickQueryFormat setJsonTextConfiguration(JsonTextConfiguration jsonTextConfiguration) {
        this.jsonTextConfiguration = jsonTextConfiguration;
        return this;
    }
}