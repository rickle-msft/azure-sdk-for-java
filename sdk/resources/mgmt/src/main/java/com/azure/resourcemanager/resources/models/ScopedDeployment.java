// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.resources.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The ScopedDeployment model. */
@Fluent
public final class ScopedDeployment {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(ScopedDeployment.class);

    /*
     * The location to store the deployment data.
     */
    @JsonProperty(value = "location", required = true)
    private String location;

    /*
     * The deployment properties.
     */
    @JsonProperty(value = "properties", required = true)
    private DeploymentProperties properties;

    /**
     * Get the location property: The location to store the deployment data.
     *
     * @return the location value.
     */
    public String location() {
        return this.location;
    }

    /**
     * Set the location property: The location to store the deployment data.
     *
     * @param location the location value to set.
     * @return the ScopedDeployment object itself.
     */
    public ScopedDeployment withLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * Get the properties property: The deployment properties.
     *
     * @return the properties value.
     */
    public DeploymentProperties properties() {
        return this.properties;
    }

    /**
     * Set the properties property: The deployment properties.
     *
     * @param properties the properties value to set.
     * @return the ScopedDeployment object itself.
     */
    public ScopedDeployment withProperties(DeploymentProperties properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (location() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property location in model ScopedDeployment"));
        }
        if (properties() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property properties in model ScopedDeployment"));
        } else {
            properties().validate();
        }
    }
}