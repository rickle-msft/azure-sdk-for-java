/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute.v2019_03_01.implementation;

import com.microsoft.azure.management.compute.v2019_03_01.OperatingSystemTypes;
import com.microsoft.azure.management.compute.v2019_03_01.OperatingSystemStateTypes;
import org.joda.time.DateTime;
import com.microsoft.azure.management.compute.v2019_03_01.GalleryImageIdentifier;
import com.microsoft.azure.management.compute.v2019_03_01.RecommendedMachineConfiguration;
import com.microsoft.azure.management.compute.v2019_03_01.Disallowed;
import com.microsoft.azure.management.compute.v2019_03_01.ImagePurchasePlan;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.Resource;

/**
 * Specifies information about the gallery Image Definition that you want to
 * create or update.
 */
@JsonFlatten
public class GalleryImageInner extends Resource {
    /**
     * The description of this gallery Image Definition resource. This property
     * is updatable.
     */
    @JsonProperty(value = "properties.description")
    private String description;

    /**
     * The Eula agreement for the gallery Image Definition.
     */
    @JsonProperty(value = "properties.eula")
    private String eula;

    /**
     * The privacy statement uri.
     */
    @JsonProperty(value = "properties.privacyStatementUri")
    private String privacyStatementUri;

    /**
     * The release note uri.
     */
    @JsonProperty(value = "properties.releaseNoteUri")
    private String releaseNoteUri;

    /**
     * This property allows you to specify the type of the OS that is included
     * in the disk when creating a VM from a managed image.
     * &lt;br&gt;&lt;br&gt; Possible values are: &lt;br&gt;&lt;br&gt;
     * **Windows** &lt;br&gt;&lt;br&gt; **Linux**. Possible values include:
     * 'Windows', 'Linux'.
     */
    @JsonProperty(value = "properties.osType", required = true)
    private OperatingSystemTypes osType;

    /**
     * This property allows the user to specify whether the virtual machines
     * created under this image are 'Generalized' or 'Specialized'. Possible
     * values include: 'Generalized', 'Specialized'.
     */
    @JsonProperty(value = "properties.osState", required = true)
    private OperatingSystemStateTypes osState;

    /**
     * The end of life date of the gallery Image Definition. This property can
     * be used for decommissioning purposes. This property is updatable.
     */
    @JsonProperty(value = "properties.endOfLifeDate")
    private DateTime endOfLifeDate;

    /**
     * The identifier property.
     */
    @JsonProperty(value = "properties.identifier", required = true)
    private GalleryImageIdentifier identifier;

    /**
     * The recommended property.
     */
    @JsonProperty(value = "properties.recommended")
    private RecommendedMachineConfiguration recommended;

    /**
     * The disallowed property.
     */
    @JsonProperty(value = "properties.disallowed")
    private Disallowed disallowed;

    /**
     * The purchasePlan property.
     */
    @JsonProperty(value = "properties.purchasePlan")
    private ImagePurchasePlan purchasePlan;

    /**
     * The current state of the gallery Image Definition.
     * The provisioning state, which only appears in the response. Possible
     * values include: 'Creating', 'Updating', 'Failed', 'Succeeded',
     * 'Deleting', 'Migrating'.
     */
    @JsonProperty(value = "properties.provisioningState", access = JsonProperty.Access.WRITE_ONLY)
    private String provisioningState;

    /**
     * Get the description of this gallery Image Definition resource. This property is updatable.
     *
     * @return the description value
     */
    public String description() {
        return this.description;
    }

    /**
     * Set the description of this gallery Image Definition resource. This property is updatable.
     *
     * @param description the description value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get the Eula agreement for the gallery Image Definition.
     *
     * @return the eula value
     */
    public String eula() {
        return this.eula;
    }

    /**
     * Set the Eula agreement for the gallery Image Definition.
     *
     * @param eula the eula value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withEula(String eula) {
        this.eula = eula;
        return this;
    }

    /**
     * Get the privacy statement uri.
     *
     * @return the privacyStatementUri value
     */
    public String privacyStatementUri() {
        return this.privacyStatementUri;
    }

    /**
     * Set the privacy statement uri.
     *
     * @param privacyStatementUri the privacyStatementUri value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withPrivacyStatementUri(String privacyStatementUri) {
        this.privacyStatementUri = privacyStatementUri;
        return this;
    }

    /**
     * Get the release note uri.
     *
     * @return the releaseNoteUri value
     */
    public String releaseNoteUri() {
        return this.releaseNoteUri;
    }

    /**
     * Set the release note uri.
     *
     * @param releaseNoteUri the releaseNoteUri value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withReleaseNoteUri(String releaseNoteUri) {
        this.releaseNoteUri = releaseNoteUri;
        return this;
    }

    /**
     * Get this property allows you to specify the type of the OS that is included in the disk when creating a VM from a managed image. &lt;br&gt;&lt;br&gt; Possible values are: &lt;br&gt;&lt;br&gt; **Windows** &lt;br&gt;&lt;br&gt; **Linux**. Possible values include: 'Windows', 'Linux'.
     *
     * @return the osType value
     */
    public OperatingSystemTypes osType() {
        return this.osType;
    }

    /**
     * Set this property allows you to specify the type of the OS that is included in the disk when creating a VM from a managed image. &lt;br&gt;&lt;br&gt; Possible values are: &lt;br&gt;&lt;br&gt; **Windows** &lt;br&gt;&lt;br&gt; **Linux**. Possible values include: 'Windows', 'Linux'.
     *
     * @param osType the osType value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withOsType(OperatingSystemTypes osType) {
        this.osType = osType;
        return this;
    }

    /**
     * Get this property allows the user to specify whether the virtual machines created under this image are 'Generalized' or 'Specialized'. Possible values include: 'Generalized', 'Specialized'.
     *
     * @return the osState value
     */
    public OperatingSystemStateTypes osState() {
        return this.osState;
    }

    /**
     * Set this property allows the user to specify whether the virtual machines created under this image are 'Generalized' or 'Specialized'. Possible values include: 'Generalized', 'Specialized'.
     *
     * @param osState the osState value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withOsState(OperatingSystemStateTypes osState) {
        this.osState = osState;
        return this;
    }

    /**
     * Get the end of life date of the gallery Image Definition. This property can be used for decommissioning purposes. This property is updatable.
     *
     * @return the endOfLifeDate value
     */
    public DateTime endOfLifeDate() {
        return this.endOfLifeDate;
    }

    /**
     * Set the end of life date of the gallery Image Definition. This property can be used for decommissioning purposes. This property is updatable.
     *
     * @param endOfLifeDate the endOfLifeDate value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withEndOfLifeDate(DateTime endOfLifeDate) {
        this.endOfLifeDate = endOfLifeDate;
        return this;
    }

    /**
     * Get the identifier value.
     *
     * @return the identifier value
     */
    public GalleryImageIdentifier identifier() {
        return this.identifier;
    }

    /**
     * Set the identifier value.
     *
     * @param identifier the identifier value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withIdentifier(GalleryImageIdentifier identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Get the recommended value.
     *
     * @return the recommended value
     */
    public RecommendedMachineConfiguration recommended() {
        return this.recommended;
    }

    /**
     * Set the recommended value.
     *
     * @param recommended the recommended value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withRecommended(RecommendedMachineConfiguration recommended) {
        this.recommended = recommended;
        return this;
    }

    /**
     * Get the disallowed value.
     *
     * @return the disallowed value
     */
    public Disallowed disallowed() {
        return this.disallowed;
    }

    /**
     * Set the disallowed value.
     *
     * @param disallowed the disallowed value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withDisallowed(Disallowed disallowed) {
        this.disallowed = disallowed;
        return this;
    }

    /**
     * Get the purchasePlan value.
     *
     * @return the purchasePlan value
     */
    public ImagePurchasePlan purchasePlan() {
        return this.purchasePlan;
    }

    /**
     * Set the purchasePlan value.
     *
     * @param purchasePlan the purchasePlan value to set
     * @return the GalleryImageInner object itself.
     */
    public GalleryImageInner withPurchasePlan(ImagePurchasePlan purchasePlan) {
        this.purchasePlan = purchasePlan;
        return this;
    }

    /**
     * Get the provisioning state, which only appears in the response. Possible values include: 'Creating', 'Updating', 'Failed', 'Succeeded', 'Deleting', 'Migrating'.
     *
     * @return the provisioningState value
     */
    public String provisioningState() {
        return this.provisioningState;
    }

}
