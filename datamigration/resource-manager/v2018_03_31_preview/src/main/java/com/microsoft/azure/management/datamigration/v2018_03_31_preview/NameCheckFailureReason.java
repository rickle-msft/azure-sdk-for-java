/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.datamigration.v2018_03_31_preview;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for NameCheckFailureReason.
 */
public final class NameCheckFailureReason extends ExpandableStringEnum<NameCheckFailureReason> {
    /** Static value AlreadyExists for NameCheckFailureReason. */
    public static final NameCheckFailureReason ALREADY_EXISTS = fromString("AlreadyExists");

    /** Static value Invalid for NameCheckFailureReason. */
    public static final NameCheckFailureReason INVALID = fromString("Invalid");

    /**
     * Creates or finds a NameCheckFailureReason from its string representation.
     * @param name a name to look for
     * @return the corresponding NameCheckFailureReason
     */
    @JsonCreator
    public static NameCheckFailureReason fromString(String name) {
        return fromString(name, NameCheckFailureReason.class);
    }

    /**
     * @return known NameCheckFailureReason values
     */
    public static Collection<NameCheckFailureReason> values() {
        return values(NameCheckFailureReason.class);
    }
}
