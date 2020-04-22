// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.storage.blob.implementation.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Map;

/**
 * An Azure Storage blob.
 */
@JacksonXmlRootElement(localName = "Blob")
@Fluent
public final class BlobItemInternal {
    /*
     * The name property.
     */
    @JsonProperty(value = "Name", required = true)
    private String name;

    /*
     * The deleted property.
     */
    @JsonProperty(value = "Deleted", required = true)
    private boolean deleted;

    /*
     * The snapshot property.
     */
    @JsonProperty(value = "Snapshot", required = true)
    private String snapshot;

    /*
     * The versionId property.
     */
    @JsonProperty(value = "VersionId", required = true)
    private String versionId;

    /*
     * The isCurrentVersion property.
     */
    @JsonProperty(value = "IsCurrentVersion")
    private Boolean isCurrentVersion;

    /*
     * The properties property.
     */
    @JsonProperty(value = "Properties", required = true)
    private BlobItemPropertiesInternal properties;

    /*
     * The metadata property.
     */
    @JsonProperty(value = "Metadata")
    private Map<String, String> metadata;

    /*
     * The objectReplicationPolicyId property.
     */
    @JsonProperty(value = "ObjectReplicationPolicyId")
    private String objectReplicationPolicyId;

    /*
     * The objectReplicationRuleStatus property.
     */
    @JsonProperty(value = "BlobObjectReplicationRuleStatus")
    private Map<String, String> objectReplicationRuleStatus;

    /*
     * The isPrefix property.
     */
    @JsonProperty(value = "IsPrefix")
    private Boolean isPrefix;

    /**
     * Get the name property: The name property.
     *
     * @return the name value.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name property: The name property.
     *
     * @param name the name value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the deleted property: The deleted property.
     *
     * @return the deleted value.
     */
    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Set the deleted property: The deleted property.
     *
     * @param deleted the deleted value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    /**
     * Get the snapshot property: The snapshot property.
     *
     * @return the snapshot value.
     */
    public String getSnapshot() {
        return this.snapshot;
    }

    /**
     * Set the snapshot property: The snapshot property.
     *
     * @param snapshot the snapshot value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setSnapshot(String snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    /**
     * Get the versionId property: The versionId property.
     *
     * @return the versionId value.
     */
    public String getVersionId() {
        return this.versionId;
    }

    /**
     * Set the versionId property: The versionId property.
     *
     * @param versionId the versionId value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    /**
     * Get the isCurrentVersion property: The isCurrentVersion property.
     *
     * @return the isCurrentVersion value.
     */
    public Boolean isCurrentVersion() {
        return this.isCurrentVersion;
    }

    /**
     * Set the isCurrentVersion property: The isCurrentVersion property.
     *
     * @param isCurrentVersion the isCurrentVersion value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setIsCurrentVersion(Boolean isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
        return this;
    }

    /**
     * Get the properties property: The properties property.
     *
     * @return the properties value.
     */
    public BlobItemPropertiesInternal getProperties() {
        return this.properties;
    }

    /**
     * Set the properties property: The properties property.
     *
     * @param properties the properties value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setProperties(BlobItemPropertiesInternal properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Get the metadata property: The metadata property.
     *
     * @return the metadata value.
     */
    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    /**
     * Set the metadata property: The metadata property.
     *
     * @param metadata the metadata value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Get the objectReplicationPolicyId property: The
     * objectReplicationPolicyId property.
     *
     * @return the objectReplicationPolicyId value.
     */
    public String getObjectReplicationPolicyId() {
        return this.objectReplicationPolicyId;
    }

    /**
     * Set the objectReplicationPolicyId property: The
     * objectReplicationPolicyId property.
     *
     * @param objectReplicationPolicyId the objectReplicationPolicyId value to
     * set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setObjectReplicationPolicyId(String objectReplicationPolicyId) {
        this.objectReplicationPolicyId = objectReplicationPolicyId;
        return this;
    }

    /**
     * Get the objectReplicationRuleStatus property: The
     * objectReplicationRuleStatus property.
     *
     * @return the objectReplicationRuleStatus value.
     */
    public Map<String, String> getObjectReplicationRuleStatus() {
        return this.objectReplicationRuleStatus;
    }

    /**
     * Set the objectReplicationRuleStatus property: The
     * objectReplicationRuleStatus property.
     *
     * @param objectReplicationRuleStatus the objectReplicationRuleStatus value
     * to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setObjectReplicationRuleStatus(Map<String, String> objectReplicationRuleStatus) {
        this.objectReplicationRuleStatus = objectReplicationRuleStatus;
        return this;
    }

    /**
     * Get the isPrefix property: The isPrefix property.
     *
     * @return the isPrefix value.
     */
    public Boolean isPrefix() {
        return this.isPrefix;
    }

    /**
     * Set the isPrefix property: The isPrefix property.
     *
     * @param isPrefix the isPrefix value to set.
     * @return the BlobItemInternal object itself.
     */
    public BlobItemInternal setIsPrefix(Boolean isPrefix) {
        this.isPrefix = isPrefix;
        return this;
    }
}
