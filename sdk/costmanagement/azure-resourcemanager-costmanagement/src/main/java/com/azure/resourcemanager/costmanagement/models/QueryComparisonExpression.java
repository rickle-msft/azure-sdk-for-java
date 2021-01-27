// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.costmanagement.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The comparison expression to be used in the query. */
@Fluent
public final class QueryComparisonExpression {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(QueryComparisonExpression.class);

    /*
     * The name of the column to use in comparison.
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    /*
     * The operator to use for comparison.
     */
    @JsonProperty(value = "operator", required = true)
    private OperatorType operator;

    /*
     * Array of values to use for comparison
     */
    @JsonProperty(value = "values", required = true)
    private List<String> values;

    /**
     * Get the name property: The name of the column to use in comparison.
     *
     * @return the name value.
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name property: The name of the column to use in comparison.
     *
     * @param name the name value to set.
     * @return the QueryComparisonExpression object itself.
     */
    public QueryComparisonExpression withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the operator property: The operator to use for comparison.
     *
     * @return the operator value.
     */
    public OperatorType operator() {
        return this.operator;
    }

    /**
     * Set the operator property: The operator to use for comparison.
     *
     * @param operator the operator value to set.
     * @return the QueryComparisonExpression object itself.
     */
    public QueryComparisonExpression withOperator(OperatorType operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Get the values property: Array of values to use for comparison.
     *
     * @return the values value.
     */
    public List<String> values() {
        return this.values;
    }

    /**
     * Set the values property: Array of values to use for comparison.
     *
     * @param values the values value to set.
     * @return the QueryComparisonExpression object itself.
     */
    public QueryComparisonExpression withValues(List<String> values) {
        this.values = values;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (name() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property name in model QueryComparisonExpression"));
        }
        if (operator() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property operator in model QueryComparisonExpression"));
        }
        if (values() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property values in model QueryComparisonExpression"));
        }
    }
}
