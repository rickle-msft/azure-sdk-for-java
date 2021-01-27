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
import java.util.List;
import java.util.Map;

/** itemAnalytics. */
@Fluent
public final class MicrosoftGraphItemAnalytics extends MicrosoftGraphEntity {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(MicrosoftGraphItemAnalytics.class);

    /*
     * itemActivityStat
     */
    @JsonProperty(value = "allTime")
    private MicrosoftGraphItemActivityStat allTime;

    /*
     * The itemActivityStats property.
     */
    @JsonProperty(value = "itemActivityStats")
    private List<MicrosoftGraphItemActivityStat> itemActivityStats;

    /*
     * itemActivityStat
     */
    @JsonProperty(value = "lastSevenDays")
    private MicrosoftGraphItemActivityStat lastSevenDays;

    /*
     * itemAnalytics
     */
    @JsonIgnore private Map<String, Object> additionalProperties;

    /**
     * Get the allTime property: itemActivityStat.
     *
     * @return the allTime value.
     */
    public MicrosoftGraphItemActivityStat allTime() {
        return this.allTime;
    }

    /**
     * Set the allTime property: itemActivityStat.
     *
     * @param allTime the allTime value to set.
     * @return the MicrosoftGraphItemAnalytics object itself.
     */
    public MicrosoftGraphItemAnalytics withAllTime(MicrosoftGraphItemActivityStat allTime) {
        this.allTime = allTime;
        return this;
    }

    /**
     * Get the itemActivityStats property: The itemActivityStats property.
     *
     * @return the itemActivityStats value.
     */
    public List<MicrosoftGraphItemActivityStat> itemActivityStats() {
        return this.itemActivityStats;
    }

    /**
     * Set the itemActivityStats property: The itemActivityStats property.
     *
     * @param itemActivityStats the itemActivityStats value to set.
     * @return the MicrosoftGraphItemAnalytics object itself.
     */
    public MicrosoftGraphItemAnalytics withItemActivityStats(List<MicrosoftGraphItemActivityStat> itemActivityStats) {
        this.itemActivityStats = itemActivityStats;
        return this;
    }

    /**
     * Get the lastSevenDays property: itemActivityStat.
     *
     * @return the lastSevenDays value.
     */
    public MicrosoftGraphItemActivityStat lastSevenDays() {
        return this.lastSevenDays;
    }

    /**
     * Set the lastSevenDays property: itemActivityStat.
     *
     * @param lastSevenDays the lastSevenDays value to set.
     * @return the MicrosoftGraphItemAnalytics object itself.
     */
    public MicrosoftGraphItemAnalytics withLastSevenDays(MicrosoftGraphItemActivityStat lastSevenDays) {
        this.lastSevenDays = lastSevenDays;
        return this;
    }

    /**
     * Get the additionalProperties property: itemAnalytics.
     *
     * @return the additionalProperties value.
     */
    @JsonAnyGetter
    public Map<String, Object> additionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Set the additionalProperties property: itemAnalytics.
     *
     * @param additionalProperties the additionalProperties value to set.
     * @return the MicrosoftGraphItemAnalytics object itself.
     */
    public MicrosoftGraphItemAnalytics withAdditionalProperties(Map<String, Object> additionalProperties) {
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
    public MicrosoftGraphItemAnalytics withId(String id) {
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
        if (allTime() != null) {
            allTime().validate();
        }
        if (itemActivityStats() != null) {
            itemActivityStats().forEach(e -> e.validate());
        }
        if (lastSevenDays() != null) {
            lastSevenDays().validate();
        }
    }
}
