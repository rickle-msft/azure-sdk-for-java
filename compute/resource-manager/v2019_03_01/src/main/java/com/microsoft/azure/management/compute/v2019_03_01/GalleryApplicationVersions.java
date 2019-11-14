/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute.v2019_03_01;

import com.microsoft.azure.arm.collection.SupportsCreating;
import rx.Completable;
import rx.Observable;
import com.microsoft.azure.management.compute.v2019_03_01.implementation.GalleryApplicationVersionsInner;
import com.microsoft.azure.arm.model.HasInner;

/**
 * Type representing GalleryApplicationVersions.
 */
public interface GalleryApplicationVersions extends SupportsCreating<GalleryApplicationVersion.DefinitionStages.Blank>, HasInner<GalleryApplicationVersionsInner> {
    /**
     * Retrieves information about a gallery Application Version.
     *
     * @param resourceGroupName The name of the resource group.
     * @param galleryName The name of the Shared Application Gallery in which the Application Definition resides.
     * @param galleryApplicationName The name of the gallery Application Definition in which the Application Version resides.
     * @param galleryApplicationVersionName The name of the gallery Application Version to be retrieved.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<GalleryApplicationVersion> getAsync(String resourceGroupName, String galleryName, String galleryApplicationName, String galleryApplicationVersionName);

    /**
     * List gallery Application Versions in a gallery Application Definition.
     *
     * @param resourceGroupName The name of the resource group.
     * @param galleryName The name of the Shared Application Gallery in which the Application Definition resides.
     * @param galleryApplicationName The name of the Shared Application Gallery Application Definition from which the Application Versions are to be listed.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<GalleryApplicationVersion> listByGalleryApplicationAsync(final String resourceGroupName, final String galleryName, final String galleryApplicationName);

    /**
     * Delete a gallery Application Version.
     *
     * @param resourceGroupName The name of the resource group.
     * @param galleryName The name of the Shared Application Gallery in which the Application Definition resides.
     * @param galleryApplicationName The name of the gallery Application Definition in which the Application Version resides.
     * @param galleryApplicationVersionName The name of the gallery Application Version to be deleted.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Completable deleteAsync(String resourceGroupName, String galleryName, String galleryApplicationName, String galleryApplicationVersionName);

}
