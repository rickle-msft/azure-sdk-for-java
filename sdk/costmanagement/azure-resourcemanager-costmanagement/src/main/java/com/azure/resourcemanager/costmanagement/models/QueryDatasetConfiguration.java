// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.costmanagement.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The configuration of dataset in the query. */
@Fluent
public final class QueryDatasetConfiguration {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(QueryDatasetConfiguration.class);

    /*
     * Array of column names to be included in the query. Any valid query
     * column name is allowed. If not provided, then query includes all
     * columns.
     */
    @JsonProperty(value = "columns")
    private List<String> columns;

    /**
     * Get the columns property: Array of column names to be included in the query. Any valid query column name is
     * allowed. If not provided, then query includes all columns.
     *
     * @return the columns value.
     */
    public List<String> columns() {
        return this.columns;
    }

    /**
     * Set the columns property: Array of column names to be included in the query. Any valid query column name is
     * allowed. If not provided, then query includes all columns.
     *
     * @param columns the columns value to set.
     * @return the QueryDatasetConfiguration object itself.
     */
    public QueryDatasetConfiguration withColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
    }
}
