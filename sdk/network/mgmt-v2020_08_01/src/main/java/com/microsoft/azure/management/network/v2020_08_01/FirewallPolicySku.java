/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network.v2020_08_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SKU of Firewall policy.
 */
public class FirewallPolicySku {
    /**
     * Tier of Firewall Policy. Possible values include: 'Standard', 'Premium'.
     */
    @JsonProperty(value = "tier")
    private FirewallPolicySkuTier tier;

    /**
     * Get tier of Firewall Policy. Possible values include: 'Standard', 'Premium'.
     *
     * @return the tier value
     */
    public FirewallPolicySkuTier tier() {
        return this.tier;
    }

    /**
     * Set tier of Firewall Policy. Possible values include: 'Standard', 'Premium'.
     *
     * @param tier the tier value to set
     * @return the FirewallPolicySku object itself.
     */
    public FirewallPolicySku withTier(FirewallPolicySkuTier tier) {
        this.tier = tier;
        return this;
    }

}
