// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.storage.file.datalake.implementation;

import com.azure.storage.file.datalake.implementation.models.ListBlobsIncludeItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

/**
 * A wrapper around List&lt;ListBlobsIncludeItem&gt; which provides top-level metadata for serialization.
 */
@JacksonXmlRootElement(localName = "ListBlobsIncludeItem")
public final class ListBlobsIncludeItemWrapper {
    @JacksonXmlProperty(localName = "ListBlobsIncludeItem")
    private final List<ListBlobsIncludeItem> listBlobsIncludeItem;

    /**
     * Creates an instance of ListBlobsIncludeItemWrapper.
     *
     * @param listBlobsIncludeItem the list.
     */
    @JsonCreator
    public ListBlobsIncludeItemWrapper(@JsonProperty("ListBlobsIncludeItem") List<ListBlobsIncludeItem> listBlobsIncludeItem) {
        this.listBlobsIncludeItem = listBlobsIncludeItem;
    }

    /**
     * Get the List&lt;ListBlobsIncludeItem&gt; contained in this wrapper.
     *
     * @return the List&lt;ListBlobsIncludeItem&gt;.
     */
    public List<ListBlobsIncludeItem> items() {
        return listBlobsIncludeItem;
    }
}
