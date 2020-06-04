// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.management.compute.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Defines values for DiskEncryptionSetIdentityType. */
public enum DiskEncryptionSetIdentityType {
    /** Enum value SystemAssigned. */
    SYSTEM_ASSIGNED("SystemAssigned");

    /** The actual serialized value for a DiskEncryptionSetIdentityType instance. */
    private final String value;

    DiskEncryptionSetIdentityType(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a DiskEncryptionSetIdentityType instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed DiskEncryptionSetIdentityType object, or null if unable to parse.
     */
    @JsonCreator
    public static DiskEncryptionSetIdentityType fromString(String value) {
        DiskEncryptionSetIdentityType[] items = DiskEncryptionSetIdentityType.values();
        for (DiskEncryptionSetIdentityType item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
