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

/** workbookChartDataLabelFormat. */
@Fluent
public final class MicrosoftGraphWorkbookChartDataLabelFormat extends MicrosoftGraphEntity {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(MicrosoftGraphWorkbookChartDataLabelFormat.class);

    /*
     * workbookChartFill
     */
    @JsonProperty(value = "fill")
    private MicrosoftGraphWorkbookChartFill fill;

    /*
     * workbookChartFont
     */
    @JsonProperty(value = "font")
    private MicrosoftGraphWorkbookChartFont font;

    /*
     * workbookChartDataLabelFormat
     */
    @JsonIgnore private Map<String, Object> additionalProperties;

    /**
     * Get the fill property: workbookChartFill.
     *
     * @return the fill value.
     */
    public MicrosoftGraphWorkbookChartFill fill() {
        return this.fill;
    }

    /**
     * Set the fill property: workbookChartFill.
     *
     * @param fill the fill value to set.
     * @return the MicrosoftGraphWorkbookChartDataLabelFormat object itself.
     */
    public MicrosoftGraphWorkbookChartDataLabelFormat withFill(MicrosoftGraphWorkbookChartFill fill) {
        this.fill = fill;
        return this;
    }

    /**
     * Get the font property: workbookChartFont.
     *
     * @return the font value.
     */
    public MicrosoftGraphWorkbookChartFont font() {
        return this.font;
    }

    /**
     * Set the font property: workbookChartFont.
     *
     * @param font the font value to set.
     * @return the MicrosoftGraphWorkbookChartDataLabelFormat object itself.
     */
    public MicrosoftGraphWorkbookChartDataLabelFormat withFont(MicrosoftGraphWorkbookChartFont font) {
        this.font = font;
        return this;
    }

    /**
     * Get the additionalProperties property: workbookChartDataLabelFormat.
     *
     * @return the additionalProperties value.
     */
    @JsonAnyGetter
    public Map<String, Object> additionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Set the additionalProperties property: workbookChartDataLabelFormat.
     *
     * @param additionalProperties the additionalProperties value to set.
     * @return the MicrosoftGraphWorkbookChartDataLabelFormat object itself.
     */
    public MicrosoftGraphWorkbookChartDataLabelFormat withAdditionalProperties(
        Map<String, Object> additionalProperties) {
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

    /** {@inheritDoc} */
    @Override
    public MicrosoftGraphWorkbookChartDataLabelFormat withId(String id) {
        super.withId(id);
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    @Override
    public void validate() {
        super.validate();
        if (fill() != null) {
            fill().validate();
        }
        if (font() != null) {
            font().validate();
        }
    }
}
