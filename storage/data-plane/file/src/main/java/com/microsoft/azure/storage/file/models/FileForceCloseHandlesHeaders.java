/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package com.microsoft.azure.storage.file.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.microsoft.rest.v2.DateTimeRfc1123;
import java.time.OffsetDateTime;

/**
 * Defines headers for ForceCloseHandles operation.
 */
@JacksonXmlRootElement(localName = "File-ForceCloseHandles-Headers")
public final class FileForceCloseHandlesHeaders {
    /**
     * This header uniquely identifies the request that was made and can be
     * used for troubleshooting the request.
     */
    @JsonProperty(value = "x-ms-request-id")
    private String requestId;

    /**
     * Indicates the version of the File service used to execute the request.
     */
    @JsonProperty(value = "x-ms-version")
    private String version;

    /**
     * A UTC date/time value generated by the service that indicates the time
     * at which the response was initiated.
     */
    @JsonProperty(value = "Date")
    private DateTimeRfc1123 date;

    /**
     * A string describing next handle to be closed. It is returned when more
     * handles need to be closed to complete the request.
     */
    @JsonProperty(value = "x-ms-marker")
    private String marker;

    /**
     * Contains count of number of handles closed.
     */
    @JsonProperty(value = "x-ms-number-of-handles-closed")
    private Integer numberOfHandlesClosed;

    /**
     * Get the requestId value.
     *
     * @return the requestId value.
     */
    public String requestId() {
        return this.requestId;
    }

    /**
     * Set the requestId value.
     *
     * @param requestId the requestId value to set.
     * @return the FileForceCloseHandlesHeaders object itself.
     */
    public FileForceCloseHandlesHeaders withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    /**
     * Get the version value.
     *
     * @return the version value.
     */
    public String version() {
        return this.version;
    }

    /**
     * Set the version value.
     *
     * @param version the version value to set.
     * @return the FileForceCloseHandlesHeaders object itself.
     */
    public FileForceCloseHandlesHeaders withVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * Get the date value.
     *
     * @return the date value.
     */
    public OffsetDateTime date() {
        if (this.date == null) {
            return null;
        }
        return this.date.dateTime();
    }

    /**
     * Set the date value.
     *
     * @param date the date value to set.
     * @return the FileForceCloseHandlesHeaders object itself.
     */
    public FileForceCloseHandlesHeaders withDate(OffsetDateTime date) {
        if (date == null) {
            this.date = null;
        } else {
            this.date = new DateTimeRfc1123(date);
        }
        return this;
    }

    /**
     * Get the marker value.
     *
     * @return the marker value.
     */
    public String marker() {
        return this.marker;
    }

    /**
     * Set the marker value.
     *
     * @param marker the marker value to set.
     * @return the FileForceCloseHandlesHeaders object itself.
     */
    public FileForceCloseHandlesHeaders withMarker(String marker) {
        this.marker = marker;
        return this;
    }

    /**
     * Get the numberOfHandlesClosed value.
     *
     * @return the numberOfHandlesClosed value.
     */
    public Integer numberOfHandlesClosed() {
        return this.numberOfHandlesClosed;
    }

    /**
     * Set the numberOfHandlesClosed value.
     *
     * @param numberOfHandlesClosed the numberOfHandlesClosed value to set.
     * @return the FileForceCloseHandlesHeaders object itself.
     */
    public FileForceCloseHandlesHeaders withNumberOfHandlesClosed(Integer numberOfHandlesClosed) {
        this.numberOfHandlesClosed = numberOfHandlesClosed;
        return this;
    }
}
