/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * abc
 */

package com.microsoft.azure.management.automation.v2015_10_31.implementation;

import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import com.microsoft.azure.management.automation.v2015_10_31.TestJobStreams;
import rx.functions.Func1;
import rx.Observable;
import com.microsoft.azure.Page;
import com.microsoft.azure.management.automation.v2015_10_31.CompilationjobAutomationAccountJobStream;

class TestJobStreamsImpl extends WrapperImpl<TestJobStreamsInner> implements TestJobStreams {
    private final AutomationManager manager;

    TestJobStreamsImpl(AutomationManager manager) {
        super(manager.inner().testJobStreams());
        this.manager = manager;
    }

    public AutomationManager manager() {
        return this.manager;
    }

    @Override
    public Observable<CompilationjobAutomationAccountJobStream> getAsync(String resourceGroupName, String automationAccountName, String runbookName, String jobStreamId) {
        TestJobStreamsInner client = this.inner();
        return client.getAsync(resourceGroupName, automationAccountName, runbookName, jobStreamId)
        .map(new Func1<JobStreamInner, CompilationjobAutomationAccountJobStream>() {
            @Override
            public CompilationjobAutomationAccountJobStream call(JobStreamInner inner) {
                return new CompilationjobAutomationAccountJobStreamImpl(inner, manager());
            }
        });
    }

    @Override
    public Observable<CompilationjobAutomationAccountJobStream> listByTestJobAsync(final String resourceGroupName, final String automationAccountName, final String runbookName) {
        TestJobStreamsInner client = this.inner();
        return client.listByTestJobAsync(resourceGroupName, automationAccountName, runbookName)
        .flatMapIterable(new Func1<Page<JobStreamInner>, Iterable<JobStreamInner>>() {
            @Override
            public Iterable<JobStreamInner> call(Page<JobStreamInner> page) {
                return page.items();
            }
        })
        .map(new Func1<JobStreamInner, CompilationjobAutomationAccountJobStream>() {
            @Override
            public CompilationjobAutomationAccountJobStream call(JobStreamInner inner) {
                return new CompilationjobAutomationAccountJobStreamImpl(inner, manager());
            }
        });
    }

}
