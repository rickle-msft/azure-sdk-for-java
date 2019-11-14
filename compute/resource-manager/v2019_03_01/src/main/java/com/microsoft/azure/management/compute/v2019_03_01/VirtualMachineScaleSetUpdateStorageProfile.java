/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute.v2019_03_01;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes a virtual machine scale set storage profile.
 */
public class VirtualMachineScaleSetUpdateStorageProfile {
    /**
     * The image reference.
     */
    @JsonProperty(value = "imageReference")
    private ImageReference imageReference;

    /**
     * The OS disk.
     */
    @JsonProperty(value = "osDisk")
    private VirtualMachineScaleSetUpdateOSDisk osDisk;

    /**
     * The data disks.
     */
    @JsonProperty(value = "dataDisks")
    private List<VirtualMachineScaleSetDataDisk> dataDisks;

    /**
     * Get the image reference.
     *
     * @return the imageReference value
     */
    public ImageReference imageReference() {
        return this.imageReference;
    }

    /**
     * Set the image reference.
     *
     * @param imageReference the imageReference value to set
     * @return the VirtualMachineScaleSetUpdateStorageProfile object itself.
     */
    public VirtualMachineScaleSetUpdateStorageProfile withImageReference(ImageReference imageReference) {
        this.imageReference = imageReference;
        return this;
    }

    /**
     * Get the OS disk.
     *
     * @return the osDisk value
     */
    public VirtualMachineScaleSetUpdateOSDisk osDisk() {
        return this.osDisk;
    }

    /**
     * Set the OS disk.
     *
     * @param osDisk the osDisk value to set
     * @return the VirtualMachineScaleSetUpdateStorageProfile object itself.
     */
    public VirtualMachineScaleSetUpdateStorageProfile withOsDisk(VirtualMachineScaleSetUpdateOSDisk osDisk) {
        this.osDisk = osDisk;
        return this;
    }

    /**
     * Get the data disks.
     *
     * @return the dataDisks value
     */
    public List<VirtualMachineScaleSetDataDisk> dataDisks() {
        return this.dataDisks;
    }

    /**
     * Set the data disks.
     *
     * @param dataDisks the dataDisks value to set
     * @return the VirtualMachineScaleSetUpdateStorageProfile object itself.
     */
    public VirtualMachineScaleSetUpdateStorageProfile withDataDisks(List<VirtualMachineScaleSetDataDisk> dataDisks) {
        this.dataDisks = dataDisks;
        return this;
    }

}
