/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 *
 */

package com.microsoft.azure.management.labservices.v2018_10_15.implementation;

import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import com.microsoft.azure.management.labservices.v2018_10_15.GalleryImages;
import rx.Completable;
import rx.Observable;
import rx.functions.Func1;
import com.microsoft.azure.Page;
import com.microsoft.azure.management.labservices.v2018_10_15.GalleryImage;

class GalleryImagesImpl extends WrapperImpl<GalleryImagesInner> implements GalleryImages {
    private final LabServicesManager manager;

    GalleryImagesImpl(LabServicesManager manager) {
        super(manager.inner().galleryImages());
        this.manager = manager;
    }

    public LabServicesManager manager() {
        return this.manager;
    }

    @Override
    public GalleryImageImpl define(String name) {
        return wrapModel(name);
    }

    private GalleryImageImpl wrapModel(GalleryImageInner inner) {
        return  new GalleryImageImpl(inner, manager());
    }

    private GalleryImageImpl wrapModel(String name) {
        return new GalleryImageImpl(name, this.manager());
    }

    @Override
    public Observable<GalleryImage> listAsync(final String resourceGroupName, final String labAccountName) {
        GalleryImagesInner client = this.inner();
        return client.listAsync(resourceGroupName, labAccountName)
        .flatMapIterable(new Func1<Page<GalleryImageInner>, Iterable<GalleryImageInner>>() {
            @Override
            public Iterable<GalleryImageInner> call(Page<GalleryImageInner> page) {
                return page.items();
            }
        })
        .map(new Func1<GalleryImageInner, GalleryImage>() {
            @Override
            public GalleryImage call(GalleryImageInner inner) {
                return wrapModel(inner);
            }
        });
    }

    @Override
    public Observable<GalleryImage> getAsync(String resourceGroupName, String labAccountName, String galleryImageName) {
        GalleryImagesInner client = this.inner();
        return client.getAsync(resourceGroupName, labAccountName, galleryImageName)
        .map(new Func1<GalleryImageInner, GalleryImage>() {
            @Override
            public GalleryImage call(GalleryImageInner inner) {
                return wrapModel(inner);
            }
       });
    }

    @Override
    public Completable deleteAsync(String resourceGroupName, String labAccountName, String galleryImageName) {
        GalleryImagesInner client = this.inner();
        return client.deleteAsync(resourceGroupName, labAccountName, galleryImageName).toCompletable();
    }

}
