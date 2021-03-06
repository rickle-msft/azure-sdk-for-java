/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.policyinsights.v2018_04_04;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Policy assignment summary.
 */
public class PolicyAssignmentSummary {
    /**
     * Policy assignment ID.
     */
    @JsonProperty(value = "policyAssignmentId")
    private String policyAssignmentId;

    /**
     * Policy set definition ID, if the policy assignment is for a policy set.
     */
    @JsonProperty(value = "policySetDefinitionId")
    private String policySetDefinitionId;

    /**
     * Non-compliance summary for the policy assignment.
     */
    @JsonProperty(value = "results")
    private SummaryResults results;

    /**
     * Policy definitions summary.
     */
    @JsonProperty(value = "policyDefinitions")
    private List<PolicyDefinitionSummary> policyDefinitions;

    /**
     * Get the policyAssignmentId value.
     *
     * @return the policyAssignmentId value
     */
    public String policyAssignmentId() {
        return this.policyAssignmentId;
    }

    /**
     * Set the policyAssignmentId value.
     *
     * @param policyAssignmentId the policyAssignmentId value to set
     * @return the PolicyAssignmentSummary object itself.
     */
    public PolicyAssignmentSummary withPolicyAssignmentId(String policyAssignmentId) {
        this.policyAssignmentId = policyAssignmentId;
        return this;
    }

    /**
     * Get the policySetDefinitionId value.
     *
     * @return the policySetDefinitionId value
     */
    public String policySetDefinitionId() {
        return this.policySetDefinitionId;
    }

    /**
     * Set the policySetDefinitionId value.
     *
     * @param policySetDefinitionId the policySetDefinitionId value to set
     * @return the PolicyAssignmentSummary object itself.
     */
    public PolicyAssignmentSummary withPolicySetDefinitionId(String policySetDefinitionId) {
        this.policySetDefinitionId = policySetDefinitionId;
        return this;
    }

    /**
     * Get the results value.
     *
     * @return the results value
     */
    public SummaryResults results() {
        return this.results;
    }

    /**
     * Set the results value.
     *
     * @param results the results value to set
     * @return the PolicyAssignmentSummary object itself.
     */
    public PolicyAssignmentSummary withResults(SummaryResults results) {
        this.results = results;
        return this;
    }

    /**
     * Get the policyDefinitions value.
     *
     * @return the policyDefinitions value
     */
    public List<PolicyDefinitionSummary> policyDefinitions() {
        return this.policyDefinitions;
    }

    /**
     * Set the policyDefinitions value.
     *
     * @param policyDefinitions the policyDefinitions value to set
     * @return the PolicyAssignmentSummary object itself.
     */
    public PolicyAssignmentSummary withPolicyDefinitions(List<PolicyDefinitionSummary> policyDefinitions) {
        this.policyDefinitions = policyDefinitions;
        return this;
    }

}
