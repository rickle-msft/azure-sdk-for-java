// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.management.compute;

import com.azure.core.annotation.ServiceClient;
import com.azure.core.http.HttpPipeline;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.util.logging.ClientLogger;
import com.azure.management.AzureServiceClient;
import com.azure.management.compute.fluent.AvailabilitySetsClient;
import com.azure.management.compute.fluent.ContainerServicesClient;
import com.azure.management.compute.fluent.DedicatedHostGroupsClient;
import com.azure.management.compute.fluent.DedicatedHostsClient;
import com.azure.management.compute.fluent.DiskEncryptionSetsClient;
import com.azure.management.compute.fluent.DisksClient;
import com.azure.management.compute.fluent.GalleriesClient;
import com.azure.management.compute.fluent.GalleryApplicationVersionsClient;
import com.azure.management.compute.fluent.GalleryApplicationsClient;
import com.azure.management.compute.fluent.GalleryImageVersionsClient;
import com.azure.management.compute.fluent.GalleryImagesClient;
import com.azure.management.compute.fluent.ImagesClient;
import com.azure.management.compute.fluent.LogAnalyticsClient;
import com.azure.management.compute.fluent.OperationsClient;
import com.azure.management.compute.fluent.ProximityPlacementGroupsClient;
import com.azure.management.compute.fluent.ResourceSkusClient;
import com.azure.management.compute.fluent.SnapshotsClient;
import com.azure.management.compute.fluent.UsagesClient;
import com.azure.management.compute.fluent.VirtualMachineExtensionImagesClient;
import com.azure.management.compute.fluent.VirtualMachineExtensionsClient;
import com.azure.management.compute.fluent.VirtualMachineImagesClient;
import com.azure.management.compute.fluent.VirtualMachineRunCommandsClient;
import com.azure.management.compute.fluent.VirtualMachineScaleSetExtensionsClient;
import com.azure.management.compute.fluent.VirtualMachineScaleSetRollingUpgradesClient;
import com.azure.management.compute.fluent.VirtualMachineScaleSetVMsClient;
import com.azure.management.compute.fluent.VirtualMachineScaleSetsClient;
import com.azure.management.compute.fluent.VirtualMachineSizesClient;
import com.azure.management.compute.fluent.VirtualMachinesClient;

/** Initializes a new instance of the ComputeManagementClient type. */
@ServiceClient(builder = ComputeManagementClientBuilder.class)
public final class ComputeManagementClient extends AzureServiceClient {
    private final ClientLogger logger = new ClientLogger(ComputeManagementClient.class);

    /**
     * Subscription credentials which uniquely identify Microsoft Azure subscription. The subscription ID forms part of
     * the URI for every service call.
     */
    private final String subscriptionId;

    /**
     * Gets Subscription credentials which uniquely identify Microsoft Azure subscription. The subscription ID forms
     * part of the URI for every service call.
     *
     * @return the subscriptionId value.
     */
    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    /** server parameter. */
    private final String endpoint;

    /**
     * Gets server parameter.
     *
     * @return the endpoint value.
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /** The HTTP pipeline to send requests through. */
    private final HttpPipeline httpPipeline;

    /**
     * Gets The HTTP pipeline to send requests through.
     *
     * @return the httpPipeline value.
     */
    public HttpPipeline getHttpPipeline() {
        return this.httpPipeline;
    }

    /** The OperationsClient object to access its operations. */
    private final OperationsClient operations;

    /**
     * Gets the OperationsClient object to access its operations.
     *
     * @return the OperationsClient object.
     */
    public OperationsClient getOperations() {
        return this.operations;
    }

    /** The AvailabilitySetsClient object to access its operations. */
    private final AvailabilitySetsClient availabilitySets;

    /**
     * Gets the AvailabilitySetsClient object to access its operations.
     *
     * @return the AvailabilitySetsClient object.
     */
    public AvailabilitySetsClient getAvailabilitySets() {
        return this.availabilitySets;
    }

    /** The ProximityPlacementGroupsClient object to access its operations. */
    private final ProximityPlacementGroupsClient proximityPlacementGroups;

    /**
     * Gets the ProximityPlacementGroupsClient object to access its operations.
     *
     * @return the ProximityPlacementGroupsClient object.
     */
    public ProximityPlacementGroupsClient getProximityPlacementGroups() {
        return this.proximityPlacementGroups;
    }

    /** The DedicatedHostGroupsClient object to access its operations. */
    private final DedicatedHostGroupsClient dedicatedHostGroups;

    /**
     * Gets the DedicatedHostGroupsClient object to access its operations.
     *
     * @return the DedicatedHostGroupsClient object.
     */
    public DedicatedHostGroupsClient getDedicatedHostGroups() {
        return this.dedicatedHostGroups;
    }

    /** The DedicatedHostsClient object to access its operations. */
    private final DedicatedHostsClient dedicatedHosts;

    /**
     * Gets the DedicatedHostsClient object to access its operations.
     *
     * @return the DedicatedHostsClient object.
     */
    public DedicatedHostsClient getDedicatedHosts() {
        return this.dedicatedHosts;
    }

    /** The VirtualMachineExtensionImagesClient object to access its operations. */
    private final VirtualMachineExtensionImagesClient virtualMachineExtensionImages;

    /**
     * Gets the VirtualMachineExtensionImagesClient object to access its operations.
     *
     * @return the VirtualMachineExtensionImagesClient object.
     */
    public VirtualMachineExtensionImagesClient getVirtualMachineExtensionImages() {
        return this.virtualMachineExtensionImages;
    }

    /** The VirtualMachineExtensionsClient object to access its operations. */
    private final VirtualMachineExtensionsClient virtualMachineExtensions;

    /**
     * Gets the VirtualMachineExtensionsClient object to access its operations.
     *
     * @return the VirtualMachineExtensionsClient object.
     */
    public VirtualMachineExtensionsClient getVirtualMachineExtensions() {
        return this.virtualMachineExtensions;
    }

    /** The VirtualMachineImagesClient object to access its operations. */
    private final VirtualMachineImagesClient virtualMachineImages;

    /**
     * Gets the VirtualMachineImagesClient object to access its operations.
     *
     * @return the VirtualMachineImagesClient object.
     */
    public VirtualMachineImagesClient getVirtualMachineImages() {
        return this.virtualMachineImages;
    }

    /** The UsagesClient object to access its operations. */
    private final UsagesClient usages;

    /**
     * Gets the UsagesClient object to access its operations.
     *
     * @return the UsagesClient object.
     */
    public UsagesClient getUsages() {
        return this.usages;
    }

    /** The VirtualMachinesClient object to access its operations. */
    private final VirtualMachinesClient virtualMachines;

    /**
     * Gets the VirtualMachinesClient object to access its operations.
     *
     * @return the VirtualMachinesClient object.
     */
    public VirtualMachinesClient getVirtualMachines() {
        return this.virtualMachines;
    }

    /** The VirtualMachineSizesClient object to access its operations. */
    private final VirtualMachineSizesClient virtualMachineSizes;

    /**
     * Gets the VirtualMachineSizesClient object to access its operations.
     *
     * @return the VirtualMachineSizesClient object.
     */
    public VirtualMachineSizesClient getVirtualMachineSizes() {
        return this.virtualMachineSizes;
    }

    /** The ImagesClient object to access its operations. */
    private final ImagesClient images;

    /**
     * Gets the ImagesClient object to access its operations.
     *
     * @return the ImagesClient object.
     */
    public ImagesClient getImages() {
        return this.images;
    }

    /** The VirtualMachineScaleSetsClient object to access its operations. */
    private final VirtualMachineScaleSetsClient virtualMachineScaleSets;

    /**
     * Gets the VirtualMachineScaleSetsClient object to access its operations.
     *
     * @return the VirtualMachineScaleSetsClient object.
     */
    public VirtualMachineScaleSetsClient getVirtualMachineScaleSets() {
        return this.virtualMachineScaleSets;
    }

    /** The VirtualMachineScaleSetExtensionsClient object to access its operations. */
    private final VirtualMachineScaleSetExtensionsClient virtualMachineScaleSetExtensions;

    /**
     * Gets the VirtualMachineScaleSetExtensionsClient object to access its operations.
     *
     * @return the VirtualMachineScaleSetExtensionsClient object.
     */
    public VirtualMachineScaleSetExtensionsClient getVirtualMachineScaleSetExtensions() {
        return this.virtualMachineScaleSetExtensions;
    }

    /** The VirtualMachineScaleSetRollingUpgradesClient object to access its operations. */
    private final VirtualMachineScaleSetRollingUpgradesClient virtualMachineScaleSetRollingUpgrades;

    /**
     * Gets the VirtualMachineScaleSetRollingUpgradesClient object to access its operations.
     *
     * @return the VirtualMachineScaleSetRollingUpgradesClient object.
     */
    public VirtualMachineScaleSetRollingUpgradesClient getVirtualMachineScaleSetRollingUpgrades() {
        return this.virtualMachineScaleSetRollingUpgrades;
    }

    /** The VirtualMachineScaleSetVMsClient object to access its operations. */
    private final VirtualMachineScaleSetVMsClient virtualMachineScaleSetVMs;

    /**
     * Gets the VirtualMachineScaleSetVMsClient object to access its operations.
     *
     * @return the VirtualMachineScaleSetVMsClient object.
     */
    public VirtualMachineScaleSetVMsClient getVirtualMachineScaleSetVMs() {
        return this.virtualMachineScaleSetVMs;
    }

    /** The LogAnalyticsClient object to access its operations. */
    private final LogAnalyticsClient logAnalytics;

    /**
     * Gets the LogAnalyticsClient object to access its operations.
     *
     * @return the LogAnalyticsClient object.
     */
    public LogAnalyticsClient getLogAnalytics() {
        return this.logAnalytics;
    }

    /** The VirtualMachineRunCommandsClient object to access its operations. */
    private final VirtualMachineRunCommandsClient virtualMachineRunCommands;

    /**
     * Gets the VirtualMachineRunCommandsClient object to access its operations.
     *
     * @return the VirtualMachineRunCommandsClient object.
     */
    public VirtualMachineRunCommandsClient getVirtualMachineRunCommands() {
        return this.virtualMachineRunCommands;
    }

    /** The ResourceSkusClient object to access its operations. */
    private final ResourceSkusClient resourceSkus;

    /**
     * Gets the ResourceSkusClient object to access its operations.
     *
     * @return the ResourceSkusClient object.
     */
    public ResourceSkusClient getResourceSkus() {
        return this.resourceSkus;
    }

    /** The DisksClient object to access its operations. */
    private final DisksClient disks;

    /**
     * Gets the DisksClient object to access its operations.
     *
     * @return the DisksClient object.
     */
    public DisksClient getDisks() {
        return this.disks;
    }

    /** The SnapshotsClient object to access its operations. */
    private final SnapshotsClient snapshots;

    /**
     * Gets the SnapshotsClient object to access its operations.
     *
     * @return the SnapshotsClient object.
     */
    public SnapshotsClient getSnapshots() {
        return this.snapshots;
    }

    /** The DiskEncryptionSetsClient object to access its operations. */
    private final DiskEncryptionSetsClient diskEncryptionSets;

    /**
     * Gets the DiskEncryptionSetsClient object to access its operations.
     *
     * @return the DiskEncryptionSetsClient object.
     */
    public DiskEncryptionSetsClient getDiskEncryptionSets() {
        return this.diskEncryptionSets;
    }

    /** The GalleriesClient object to access its operations. */
    private final GalleriesClient galleries;

    /**
     * Gets the GalleriesClient object to access its operations.
     *
     * @return the GalleriesClient object.
     */
    public GalleriesClient getGalleries() {
        return this.galleries;
    }

    /** The GalleryImagesClient object to access its operations. */
    private final GalleryImagesClient galleryImages;

    /**
     * Gets the GalleryImagesClient object to access its operations.
     *
     * @return the GalleryImagesClient object.
     */
    public GalleryImagesClient getGalleryImages() {
        return this.galleryImages;
    }

    /** The GalleryImageVersionsClient object to access its operations. */
    private final GalleryImageVersionsClient galleryImageVersions;

    /**
     * Gets the GalleryImageVersionsClient object to access its operations.
     *
     * @return the GalleryImageVersionsClient object.
     */
    public GalleryImageVersionsClient getGalleryImageVersions() {
        return this.galleryImageVersions;
    }

    /** The GalleryApplicationsClient object to access its operations. */
    private final GalleryApplicationsClient galleryApplications;

    /**
     * Gets the GalleryApplicationsClient object to access its operations.
     *
     * @return the GalleryApplicationsClient object.
     */
    public GalleryApplicationsClient getGalleryApplications() {
        return this.galleryApplications;
    }

    /** The GalleryApplicationVersionsClient object to access its operations. */
    private final GalleryApplicationVersionsClient galleryApplicationVersions;

    /**
     * Gets the GalleryApplicationVersionsClient object to access its operations.
     *
     * @return the GalleryApplicationVersionsClient object.
     */
    public GalleryApplicationVersionsClient getGalleryApplicationVersions() {
        return this.galleryApplicationVersions;
    }

    /** The ContainerServicesClient object to access its operations. */
    private final ContainerServicesClient containerServices;

    /**
     * Gets the ContainerServicesClient object to access its operations.
     *
     * @return the ContainerServicesClient object.
     */
    public ContainerServicesClient getContainerServices() {
        return this.containerServices;
    }

    /**
     * Initializes an instance of ComputeManagementClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param environment The Azure environment.
     */
    ComputeManagementClient(
        HttpPipeline httpPipeline, AzureEnvironment environment, String subscriptionId, String endpoint) {
        super(httpPipeline, environment);
        this.httpPipeline = httpPipeline;
        this.subscriptionId = subscriptionId;
        this.endpoint = endpoint;
        this.operations = new OperationsClient(this);
        this.availabilitySets = new AvailabilitySetsClient(this);
        this.proximityPlacementGroups = new ProximityPlacementGroupsClient(this);
        this.dedicatedHostGroups = new DedicatedHostGroupsClient(this);
        this.dedicatedHosts = new DedicatedHostsClient(this);
        this.virtualMachineExtensionImages = new VirtualMachineExtensionImagesClient(this);
        this.virtualMachineExtensions = new VirtualMachineExtensionsClient(this);
        this.virtualMachineImages = new VirtualMachineImagesClient(this);
        this.usages = new UsagesClient(this);
        this.virtualMachines = new VirtualMachinesClient(this);
        this.virtualMachineSizes = new VirtualMachineSizesClient(this);
        this.images = new ImagesClient(this);
        this.virtualMachineScaleSets = new VirtualMachineScaleSetsClient(this);
        this.virtualMachineScaleSetExtensions = new VirtualMachineScaleSetExtensionsClient(this);
        this.virtualMachineScaleSetRollingUpgrades = new VirtualMachineScaleSetRollingUpgradesClient(this);
        this.virtualMachineScaleSetVMs = new VirtualMachineScaleSetVMsClient(this);
        this.logAnalytics = new LogAnalyticsClient(this);
        this.virtualMachineRunCommands = new VirtualMachineRunCommandsClient(this);
        this.resourceSkus = new ResourceSkusClient(this);
        this.disks = new DisksClient(this);
        this.snapshots = new SnapshotsClient(this);
        this.diskEncryptionSets = new DiskEncryptionSetsClient(this);
        this.galleries = new GalleriesClient(this);
        this.galleryImages = new GalleryImagesClient(this);
        this.galleryImageVersions = new GalleryImageVersionsClient(this);
        this.galleryApplications = new GalleryApplicationsClient(this);
        this.galleryApplicationVersions = new GalleryApplicationVersionsClient(this);
        this.containerServices = new ContainerServicesClient(this);
    }
}
