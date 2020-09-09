// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.messaging.eventgrid.systemevents;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The DeviceTwinInfoX509Thumbprint model. */
@Fluent
public final class DeviceTwinInfoX509Thumbprint {
    /*
     * Primary thumbprint for the x509 certificate.
     */
    @JsonProperty(value = "primaryThumbprint")
    private String primaryThumbprint;

    /*
     * Secondary thumbprint for the x509 certificate.
     */
    @JsonProperty(value = "secondaryThumbprint")
    private String secondaryThumbprint;

    /**
     * Get the primaryThumbprint property: Primary thumbprint for the x509 certificate.
     *
     * @return the primaryThumbprint value.
     */
    public String getPrimaryThumbprint() {
        return this.primaryThumbprint;
    }

    /**
     * Set the primaryThumbprint property: Primary thumbprint for the x509 certificate.
     *
     * @param primaryThumbprint the primaryThumbprint value to set.
     * @return the DeviceTwinInfoX509Thumbprint object itself.
     */
    public DeviceTwinInfoX509Thumbprint setPrimaryThumbprint(String primaryThumbprint) {
        this.primaryThumbprint = primaryThumbprint;
        return this;
    }

    /**
     * Get the secondaryThumbprint property: Secondary thumbprint for the x509 certificate.
     *
     * @return the secondaryThumbprint value.
     */
    public String getSecondaryThumbprint() {
        return this.secondaryThumbprint;
    }

    /**
     * Set the secondaryThumbprint property: Secondary thumbprint for the x509 certificate.
     *
     * @param secondaryThumbprint the secondaryThumbprint value to set.
     * @return the DeviceTwinInfoX509Thumbprint object itself.
     */
    public DeviceTwinInfoX509Thumbprint setSecondaryThumbprint(String secondaryThumbprint) {
        this.secondaryThumbprint = secondaryThumbprint;
        return this;
    }
}
