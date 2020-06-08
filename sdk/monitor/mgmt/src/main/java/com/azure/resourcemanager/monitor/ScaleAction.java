// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.monitor;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;

/** The ScaleAction model. */
@Fluent
public final class ScaleAction {
    @JsonIgnore private final ClientLogger logger = new ClientLogger(ScaleAction.class);

    /*
     * the scale direction. Whether the scaling action increases or decreases
     * the number of instances.
     */
    @JsonProperty(value = "direction", required = true)
    private ScaleDirection direction;

    /*
     * the type of action that should occur when the scale rule fires.
     */
    @JsonProperty(value = "type", required = true)
    private ScaleType type;

    /*
     * the number of instances that are involved in the scaling action. This
     * value must be 1 or greater. The default value is 1.
     */
    @JsonProperty(value = "value")
    private String value;

    /*
     * the amount of time to wait since the last scaling action before this
     * action occurs. It must be between 1 week and 1 minute in ISO 8601
     * format.
     */
    @JsonProperty(value = "cooldown", required = true)
    private Duration cooldown;

    /**
     * Get the direction property: the scale direction. Whether the scaling action increases or decreases the number of
     * instances.
     *
     * @return the direction value.
     */
    public ScaleDirection direction() {
        return this.direction;
    }

    /**
     * Set the direction property: the scale direction. Whether the scaling action increases or decreases the number of
     * instances.
     *
     * @param direction the direction value to set.
     * @return the ScaleAction object itself.
     */
    public ScaleAction withDirection(ScaleDirection direction) {
        this.direction = direction;
        return this;
    }

    /**
     * Get the type property: the type of action that should occur when the scale rule fires.
     *
     * @return the type value.
     */
    public ScaleType type() {
        return this.type;
    }

    /**
     * Set the type property: the type of action that should occur when the scale rule fires.
     *
     * @param type the type value to set.
     * @return the ScaleAction object itself.
     */
    public ScaleAction withType(ScaleType type) {
        this.type = type;
        return this;
    }

    /**
     * Get the value property: the number of instances that are involved in the scaling action. This value must be 1 or
     * greater. The default value is 1.
     *
     * @return the value value.
     */
    public String value() {
        return this.value;
    }

    /**
     * Set the value property: the number of instances that are involved in the scaling action. This value must be 1 or
     * greater. The default value is 1.
     *
     * @param value the value value to set.
     * @return the ScaleAction object itself.
     */
    public ScaleAction withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Get the cooldown property: the amount of time to wait since the last scaling action before this action occurs. It
     * must be between 1 week and 1 minute in ISO 8601 format.
     *
     * @return the cooldown value.
     */
    public Duration cooldown() {
        return this.cooldown;
    }

    /**
     * Set the cooldown property: the amount of time to wait since the last scaling action before this action occurs. It
     * must be between 1 week and 1 minute in ISO 8601 format.
     *
     * @param cooldown the cooldown value to set.
     * @return the ScaleAction object itself.
     */
    public ScaleAction withCooldown(Duration cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (direction() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property direction in model ScaleAction"));
        }
        if (type() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property type in model ScaleAction"));
        }
        if (cooldown() == null) {
            throw logger
                .logExceptionAsError(
                    new IllegalArgumentException("Missing required property cooldown in model ScaleAction"));
        }
    }
}