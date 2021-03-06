/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.logic.v2016_06_01;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for EdifactDecimalIndicator.
 */
public enum EdifactDecimalIndicator {
    /** Enum value NotSpecified. */
    NOT_SPECIFIED("NotSpecified"),

    /** Enum value Comma. */
    COMMA("Comma"),

    /** Enum value Decimal. */
    DECIMAL("Decimal");

    /** The actual serialized value for a EdifactDecimalIndicator instance. */
    private String value;

    EdifactDecimalIndicator(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a EdifactDecimalIndicator instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed EdifactDecimalIndicator object, or null if unable to parse.
     */
    @JsonCreator
    public static EdifactDecimalIndicator fromString(String value) {
        EdifactDecimalIndicator[] items = EdifactDecimalIndicator.values();
        for (EdifactDecimalIndicator item : items) {
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
