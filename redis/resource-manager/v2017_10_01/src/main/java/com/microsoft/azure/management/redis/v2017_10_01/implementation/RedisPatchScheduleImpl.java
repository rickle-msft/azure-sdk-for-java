/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.redis.v2017_10_01.implementation;

import com.microsoft.azure.management.redis.v2017_10_01.RedisPatchSchedule;
import com.microsoft.azure.arm.model.implementation.CreatableUpdatableImpl;
import rx.Observable;
import java.util.List;
import com.microsoft.azure.management.redis.v2017_10_01.ScheduleEntry;

class RedisPatchScheduleImpl extends CreatableUpdatableImpl<RedisPatchSchedule, RedisPatchScheduleInner, RedisPatchScheduleImpl> implements RedisPatchSchedule, RedisPatchSchedule.Definition, RedisPatchSchedule.Update {
    private final RedisManager manager;
    private String resourceGroupName;
    private String name;
    private List<ScheduleEntry> cscheduleEntries;
    private List<ScheduleEntry> uscheduleEntries;

    RedisPatchScheduleImpl(String name, RedisManager manager) {
        super(name, new RedisPatchScheduleInner());
        this.manager = manager;
        // Set resource name
        this.name = name;
        //
    }

    RedisPatchScheduleImpl(RedisPatchScheduleInner inner, RedisManager manager) {
        super(inner.name(), inner);
        this.manager = manager;
        // Set resource name
        this.name = inner.name();
        // resource ancestor names
        this.resourceGroupName = IdParsingUtils.getValueFromIdByName(inner.id(), "resourceGroups");
        this.name = IdParsingUtils.getValueFromIdByName(inner.id(), "Redis");
        //
    }

    @Override
    public RedisManager manager() {
        return this.manager;
    }

    @Override
    public Observable<RedisPatchSchedule> createResourceAsync() {
        PatchSchedulesInner client = this.manager().inner().patchSchedules();
        return client.createOrUpdateAsync(this.resourceGroupName, this.name, this.cscheduleEntries)
            .map(innerToFluentMap(this));
    }

    @Override
    public Observable<RedisPatchSchedule> updateResourceAsync() {
        PatchSchedulesInner client = this.manager().inner().patchSchedules();
        return client.createOrUpdateAsync(this.resourceGroupName, this.name, this.uscheduleEntries)
            .map(innerToFluentMap(this));
    }

    @Override
    protected Observable<RedisPatchScheduleInner> getInnerAsync() {
        PatchSchedulesInner client = this.manager().inner().patchSchedules();
        return client.getAsync(this.resourceGroupName, this.name);
    }

    @Override
    public boolean isInCreateMode() {
        return this.inner().id() == null;
    }


    @Override
    public String id() {
        return this.inner().id();
    }

    @Override
    public String name() {
        return this.inner().name();
    }

    @Override
    public List<ScheduleEntry> scheduleEntries() {
        return this.inner().scheduleEntries();
    }

    @Override
    public String type() {
        return this.inner().type();
    }

    @Override
    public RedisPatchScheduleImpl withExistingRedis(String resourceGroupName, String name) {
        this.resourceGroupName = resourceGroupName;
        this.name = name;
        return this;
    }

    @Override
    public RedisPatchScheduleImpl withScheduleEntries(List<ScheduleEntry> scheduleEntries) {
        if (isInCreateMode()) {
            this.cscheduleEntries = scheduleEntries;
        } else {
            this.uscheduleEntries = scheduleEntries;
        }
        return this;
    }

}
