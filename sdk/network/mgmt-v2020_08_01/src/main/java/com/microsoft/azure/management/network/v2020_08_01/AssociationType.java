/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network.v2020_08_01;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for AssociationType.
 */
public final class AssociationType extends ExpandableStringEnum<AssociationType> {
    /** Static value Associated for AssociationType. */
    public static final AssociationType ASSOCIATED = fromString("Associated");

    /** Static value Contains for AssociationType. */
    public static final AssociationType CONTAINS = fromString("Contains");

    /**
     * Creates or finds a AssociationType from its string representation.
     * @param name a name to look for
     * @return the corresponding AssociationType
     */
    @JsonCreator
    public static AssociationType fromString(String name) {
        return fromString(name, AssociationType.class);
    }

    /**
     * @return known AssociationType values
     */
    public static Collection<AssociationType> values() {
        return values(AssociationType.class);
    }
}
