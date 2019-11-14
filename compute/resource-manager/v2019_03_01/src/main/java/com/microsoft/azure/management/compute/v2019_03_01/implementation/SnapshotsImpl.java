/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * def
 */

package com.microsoft.azure.management.compute.v2019_03_01.implementation;

import com.microsoft.azure.arm.resources.collection.implementation.GroupableResourcesCoreImpl;
import com.microsoft.azure.management.compute.v2019_03_01.Snapshots;
import com.microsoft.azure.management.compute.v2019_03_01.Snapshot;
import rx.Observable;
import rx.Completable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import com.microsoft.azure.arm.resources.ResourceUtilsCore;
import com.microsoft.azure.arm.utils.RXMapper;
import rx.functions.Func1;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.Page;
import com.microsoft.azure.management.compute.v2019_03_01.AccessUri;
import com.microsoft.azure.management.compute.v2019_03_01.GrantAccessData;

class SnapshotsImpl extends GroupableResourcesCoreImpl<Snapshot, SnapshotImpl, SnapshotInner, SnapshotsInner, ComputeManager>  implements Snapshots {
    protected SnapshotsImpl(ComputeManager manager) {
        super(manager.inner().snapshots(), manager);
    }

    @Override
    protected Observable<SnapshotInner> getInnerAsync(String resourceGroupName, String name) {
        SnapshotsInner client = this.inner();
        return client.getByResourceGroupAsync(resourceGroupName, name);
    }

    @Override
    protected Completable deleteInnerAsync(String resourceGroupName, String name) {
        SnapshotsInner client = this.inner();
        return client.deleteAsync(resourceGroupName, name).toCompletable();
    }

    @Override
    public Observable<String> deleteByIdsAsync(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Observable.empty();
        }
        Collection<Observable<String>> observables = new ArrayList<>();
        for (String id : ids) {
            final String resourceGroupName = ResourceUtilsCore.groupFromResourceId(id);
            final String name = ResourceUtilsCore.nameFromResourceId(id);
            Observable<String> o = RXMapper.map(this.inner().deleteAsync(resourceGroupName, name), id);
            observables.add(o);
        }
        return Observable.mergeDelayError(observables);
    }

    @Override
    public Observable<String> deleteByIdsAsync(String...ids) {
        return this.deleteByIdsAsync(new ArrayList<String>(Arrays.asList(ids)));
    }

    @Override
    public void deleteByIds(Collection<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.deleteByIdsAsync(ids).toBlocking().last();
        }
    }

    @Override
    public void deleteByIds(String...ids) {
        this.deleteByIds(new ArrayList<String>(Arrays.asList(ids)));
    }

    @Override
    public PagedList<Snapshot> listByResourceGroup(String resourceGroupName) {
        SnapshotsInner client = this.inner();
        return this.wrapList(client.listByResourceGroup(resourceGroupName));
    }

    @Override
    public Observable<Snapshot> listByResourceGroupAsync(String resourceGroupName) {
        SnapshotsInner client = this.inner();
        return client.listByResourceGroupAsync(resourceGroupName)
        .flatMapIterable(new Func1<Page<SnapshotInner>, Iterable<SnapshotInner>>() {
            @Override
            public Iterable<SnapshotInner> call(Page<SnapshotInner> page) {
                return page.items();
            }
        })
        .map(new Func1<SnapshotInner, Snapshot>() {
            @Override
            public Snapshot call(SnapshotInner inner) {
                return wrapModel(inner);
            }
        });
    }

    @Override
    public PagedList<Snapshot> list() {
        SnapshotsInner client = this.inner();
        return this.wrapList(client.list());
    }

    @Override
    public Observable<Snapshot> listAsync() {
        SnapshotsInner client = this.inner();
        return client.listAsync()
        .flatMapIterable(new Func1<Page<SnapshotInner>, Iterable<SnapshotInner>>() {
            @Override
            public Iterable<SnapshotInner> call(Page<SnapshotInner> page) {
                return page.items();
            }
        })
        .map(new Func1<SnapshotInner, Snapshot>() {
            @Override
            public Snapshot call(SnapshotInner inner) {
                return wrapModel(inner);
            }
        });
    }

    @Override
    public SnapshotImpl define(String name) {
        return wrapModel(name);
    }

    @Override
    public Observable<AccessUri> grantAccessAsync(String resourceGroupName, String snapshotName, GrantAccessData grantAccessData) {
        SnapshotsInner client = this.inner();
        return client.grantAccessAsync(resourceGroupName, snapshotName, grantAccessData)
        .map(new Func1<AccessUriInner, AccessUri>() {
            @Override
            public AccessUri call(AccessUriInner inner) {
                return new AccessUriImpl(inner, manager());
            }
        });
    }

    @Override
    public Completable revokeAccessAsync(String resourceGroupName, String snapshotName) {
        SnapshotsInner client = this.inner();
        return client.revokeAccessAsync(resourceGroupName, snapshotName).toCompletable();
    }

    @Override
    protected SnapshotImpl wrapModel(SnapshotInner inner) {
        return  new SnapshotImpl(inner.name(), inner, manager());
    }

    @Override
    protected SnapshotImpl wrapModel(String name) {
        return new SnapshotImpl(name, new SnapshotInner(), this.manager());
    }

}
