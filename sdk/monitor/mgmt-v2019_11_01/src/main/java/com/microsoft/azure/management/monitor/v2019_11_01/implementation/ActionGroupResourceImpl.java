/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.monitor.v2019_11_01.implementation;

import com.microsoft.azure.arm.resources.models.implementation.GroupableResourceCoreImpl;
import com.microsoft.azure.management.monitor.v2019_11_01.ActionGroupResource;
import rx.Observable;
import com.microsoft.azure.management.monitor.v2019_11_01.ActionGroupPatchBody;
import java.util.List;
import com.microsoft.azure.management.monitor.v2019_11_01.EmailReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.SmsReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.WebhookReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.ItsmReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.AzureAppPushReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.AutomationRunbookReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.VoiceReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.LogicAppReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.AzureFunctionReceiver;
import com.microsoft.azure.management.monitor.v2019_11_01.ArmRoleReceiver;
import rx.functions.Func1;

class ActionGroupResourceImpl extends GroupableResourceCoreImpl<ActionGroupResource, ActionGroupResourceInner, ActionGroupResourceImpl, MonitorManager> implements ActionGroupResource, ActionGroupResource.Definition, ActionGroupResource.Update {
    private ActionGroupPatchBody updateParameter;
    ActionGroupResourceImpl(String name, ActionGroupResourceInner inner, MonitorManager manager) {
        super(name, inner, manager);
        this.updateParameter = new ActionGroupPatchBody();
    }

    @Override
    public Observable<ActionGroupResource> createResourceAsync() {
        ActionGroupsInner client = this.manager().inner().actionGroups();
        return client.createOrUpdateAsync(this.resourceGroupName(), this.name(), this.inner())
            .map(new Func1<ActionGroupResourceInner, ActionGroupResourceInner>() {
               @Override
               public ActionGroupResourceInner call(ActionGroupResourceInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    public Observable<ActionGroupResource> updateResourceAsync() {
        ActionGroupsInner client = this.manager().inner().actionGroups();
        return client.updateAsync(this.resourceGroupName(), this.name(), this.updateParameter)
            .map(new Func1<ActionGroupResourceInner, ActionGroupResourceInner>() {
               @Override
               public ActionGroupResourceInner call(ActionGroupResourceInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    protected Observable<ActionGroupResourceInner> getInnerAsync() {
        ActionGroupsInner client = this.manager().inner().actionGroups();
        return client.getByResourceGroupAsync(this.resourceGroupName(), this.name());
    }

    @Override
    public boolean isInCreateMode() {
        return this.inner().id() == null;
    }

    private void resetCreateUpdateParameters() {
        this.updateParameter = new ActionGroupPatchBody();
    }

    @Override
    public List<ArmRoleReceiver> armRoleReceivers() {
        return this.inner().armRoleReceivers();
    }

    @Override
    public List<AutomationRunbookReceiver> automationRunbookReceivers() {
        return this.inner().automationRunbookReceivers();
    }

    @Override
    public List<AzureAppPushReceiver> azureAppPushReceivers() {
        return this.inner().azureAppPushReceivers();
    }

    @Override
    public List<AzureFunctionReceiver> azureFunctionReceivers() {
        return this.inner().azureFunctionReceivers();
    }

    @Override
    public List<EmailReceiver> emailReceivers() {
        return this.inner().emailReceivers();
    }

    @Override
    public boolean enabled() {
        return this.inner().enabled();
    }

    @Override
    public String groupShortName() {
        return this.inner().groupShortName();
    }

    @Override
    public List<ItsmReceiver> itsmReceivers() {
        return this.inner().itsmReceivers();
    }

    @Override
    public List<LogicAppReceiver> logicAppReceivers() {
        return this.inner().logicAppReceivers();
    }

    @Override
    public List<SmsReceiver> smsReceivers() {
        return this.inner().smsReceivers();
    }

    @Override
    public List<VoiceReceiver> voiceReceivers() {
        return this.inner().voiceReceivers();
    }

    @Override
    public List<WebhookReceiver> webhookReceivers() {
        return this.inner().webhookReceivers();
    }

    @Override
    public ActionGroupResourceImpl withEnabled(boolean enabled) {
        this.inner().withEnabled(enabled);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withGroupShortName(String groupShortName) {
        this.inner().withGroupShortName(groupShortName);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withArmRoleReceivers(List<ArmRoleReceiver> armRoleReceivers) {
        this.inner().withArmRoleReceivers(armRoleReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withAutomationRunbookReceivers(List<AutomationRunbookReceiver> automationRunbookReceivers) {
        this.inner().withAutomationRunbookReceivers(automationRunbookReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withAzureAppPushReceivers(List<AzureAppPushReceiver> azureAppPushReceivers) {
        this.inner().withAzureAppPushReceivers(azureAppPushReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withAzureFunctionReceivers(List<AzureFunctionReceiver> azureFunctionReceivers) {
        this.inner().withAzureFunctionReceivers(azureFunctionReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withEmailReceivers(List<EmailReceiver> emailReceivers) {
        this.inner().withEmailReceivers(emailReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withItsmReceivers(List<ItsmReceiver> itsmReceivers) {
        this.inner().withItsmReceivers(itsmReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withLogicAppReceivers(List<LogicAppReceiver> logicAppReceivers) {
        this.inner().withLogicAppReceivers(logicAppReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withSmsReceivers(List<SmsReceiver> smsReceivers) {
        this.inner().withSmsReceivers(smsReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withVoiceReceivers(List<VoiceReceiver> voiceReceivers) {
        this.inner().withVoiceReceivers(voiceReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withWebhookReceivers(List<WebhookReceiver> webhookReceivers) {
        this.inner().withWebhookReceivers(webhookReceivers);
        return this;
    }

    @Override
    public ActionGroupResourceImpl withEnabled(Boolean enabled) {
        this.updateParameter.withEnabled(enabled);
        return this;
    }

}
