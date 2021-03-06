// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.azure.storage.blob;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains metadata key/value pairs to be associated with a storage resource. The user may store any additional
 * information about the resource that they like using this map. It is passed to create and setMetadata methods on any
 * URL type. Null may be passed to set no metadata.
 */
public final class Metadata extends HashMap<String, String> {

    public Metadata() {
        super();
    }

    public Metadata(Map<? extends String, ? extends String> m) {
        super(m);
    }
}
