// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.monitor.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.annotation.JsonFlatten;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.monitor.TimeSeriesBaseline;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;

/** The SingleMetricBaseline model. */
@JsonFlatten
@Fluent
public class SingleMetricBaselineInner {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(SingleMetricBaselineInner.class);

    /*
     * The metric baseline Id.
     */
    @JsonProperty(value = "id", required = true)
    private String id;

    /*
     * The resource type of the metric baseline resource.
     */
    @JsonProperty(value = "type", required = true)
    private String type;

    /*
     * The name of the metric for which the baselines were retrieved.
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    /*
     * The timespan for which the data was retrieved. Its value consists of two
     * datetimes concatenated, separated by '/'.  This may be adjusted in the
     * future and returned back from what was originally requested.
     */
    @JsonProperty(value = "properties.timespan", required = true)
    private String timespan;

    /*
     * The interval (window size) for which the metric data was returned in.
     * This may be adjusted in the future and returned back from what was
     * originally requested.  This is not present if a metadata request was
     * made.
     */
    @JsonProperty(value = "properties.interval", required = true)
    private Duration interval;

    /*
     * The namespace of the metrics been queried.
     */
    @JsonProperty(value = "properties.namespace")
    private String namespace;

    /*
     * The baseline for each time series that was queried.
     */
    @JsonProperty(value = "properties.baselines", required = true)
    private List<TimeSeriesBaseline> baselines;

    /**
     * Get the id property: The metric baseline Id.
     *
     * @return the id value.
     */
    public String id() {
        return this.id;
    }

    /**
     * Set the id property: The metric baseline Id.
     *
     * @param id the id value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get the type property: The resource type of the metric baseline resource.
     *
     * @return the type value.
     */
    public String type() {
        return this.type;
    }

    /**
     * Set the type property: The resource type of the metric baseline resource.
     *
     * @param type the type value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the name property: The name of the metric for which the baselines were retrieved.
     *
     * @return the name value.
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name property: The name of the metric for which the baselines were retrieved.
     *
     * @param name the name value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the timespan property: The timespan for which the data was retrieved. Its value consists of two datetimes
     * concatenated, separated by '/'. This may be adjusted in the future and returned back from what was originally
     * requested.
     *
     * @return the timespan value.
     */
    public String timespan() {
        return this.timespan;
    }

    /**
     * Set the timespan property: The timespan for which the data was retrieved. Its value consists of two datetimes
     * concatenated, separated by '/'. This may be adjusted in the future and returned back from what was originally
     * requested.
     *
     * @param timespan the timespan value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withTimespan(String timespan) {
        this.timespan = timespan;
        return this;
    }

    /**
     * Get the interval property: The interval (window size) for which the metric data was returned in. This may be
     * adjusted in the future and returned back from what was originally requested. This is not present if a metadata
     * request was made.
     *
     * @return the interval value.
     */
    public Duration interval() {
        return this.interval;
    }

    /**
     * Set the interval property: The interval (window size) for which the metric data was returned in. This may be
     * adjusted in the future and returned back from what was originally requested. This is not present if a metadata
     * request was made.
     *
     * @param interval the interval value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withInterval(Duration interval) {
        this.interval = interval;
        return this;
    }

    /**
     * Get the namespace property: The namespace of the metrics been queried.
     *
     * @return the namespace value.
     */
    public String namespace() {
        return this.namespace;
    }

    /**
     * Set the namespace property: The namespace of the metrics been queried.
     *
     * @param namespace the namespace value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * Get the baselines property: The baseline for each time series that was queried.
     *
     * @return the baselines value.
     */
    public List<TimeSeriesBaseline> baselines() {
        return this.baselines;
    }

    /**
     * Set the baselines property: The baseline for each time series that was queried.
     *
     * @param baselines the baselines value to set.
     * @return the SingleMetricBaselineInner object itself.
     */
    public SingleMetricBaselineInner withBaselines(List<TimeSeriesBaseline> baselines) {
        this.baselines = baselines;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (id() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property id in model SingleMetricBaselineInner"));
        }
        if (type() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property type in model SingleMetricBaselineInner"));
        }
        if (name() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property name in model SingleMetricBaselineInner"));
        }
        if (timespan() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property timespan in model SingleMetricBaselineInner"));
        }
        if (interval() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property interval in model SingleMetricBaselineInner"));
        }
        if (baselines() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property baselines in model SingleMetricBaselineInner"));
        } else {
            baselines().forEach(e -> e.validate());
        }
    }
}