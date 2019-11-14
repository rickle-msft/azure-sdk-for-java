/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.mediaservices.v2019_05_01_preview;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for LiveEventInputProtocol.
 */
public final class LiveEventInputProtocol extends ExpandableStringEnum<LiveEventInputProtocol> {
    /** Static value FragmentedMP4 for LiveEventInputProtocol. */
    public static final LiveEventInputProtocol FRAGMENTED_MP4 = fromString("FragmentedMP4");

    /** Static value RTMP for LiveEventInputProtocol. */
    public static final LiveEventInputProtocol RTMP = fromString("RTMP");

    /**
     * Creates or finds a LiveEventInputProtocol from its string representation.
     * @param name a name to look for
     * @return the corresponding LiveEventInputProtocol
     */
    @JsonCreator
    public static LiveEventInputProtocol fromString(String name) {
        return fromString(name, LiveEventInputProtocol.class);
    }

    /**
     * @return known LiveEventInputProtocol values
     */
    public static Collection<LiveEventInputProtocol> values() {
        return values(LiveEventInputProtocol.class);
    }
}
