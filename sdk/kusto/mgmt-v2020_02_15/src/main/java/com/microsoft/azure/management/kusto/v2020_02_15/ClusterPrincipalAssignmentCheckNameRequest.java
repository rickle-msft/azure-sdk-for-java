/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.kusto.v2020_02_15;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A principal assignment check name availability request.
 */
public class ClusterPrincipalAssignmentCheckNameRequest {
    /**
     * Principal Assignment resource name.
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    /**
     * The type of resource, Microsoft.Kusto/clusters/principalAssignments.
     */
    @JsonProperty(value = "type", required = true)
    private String type;

    /**
     * Creates an instance of ClusterPrincipalAssignmentCheckNameRequest class.
     * @param name principal Assignment resource name.
     */
    public ClusterPrincipalAssignmentCheckNameRequest() {
        type = "Microsoft.Kusto/clusters/principalAssignments";
    }

    /**
     * Get principal Assignment resource name.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set principal Assignment resource name.
     *
     * @param name the name value to set
     * @return the ClusterPrincipalAssignmentCheckNameRequest object itself.
     */
    public ClusterPrincipalAssignmentCheckNameRequest withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the type of resource, Microsoft.Kusto/clusters/principalAssignments.
     *
     * @return the type value
     */
    public String type() {
        return this.type;
    }

    /**
     * Set the type of resource, Microsoft.Kusto/clusters/principalAssignments.
     *
     * @param type the type value to set
     * @return the ClusterPrincipalAssignmentCheckNameRequest object itself.
     */
    public ClusterPrincipalAssignmentCheckNameRequest withType(String type) {
        this.type = type;
        return this;
    }

}