// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.core.amqp;

import com.azure.core.credential.TokenCredential;
import reactor.core.publisher.Mono;

import java.io.Closeable;
import java.time.OffsetDateTime;

/**
 * Claims-based security (CBS) node that authorizes connections with AMQP services.
 *
 * @see <a href="https://www.oasis-open.org/committees/download.php/62097/amqp-cbs-v1.0-wd05.doc">
 * AMPQ Claims-based Security v1.0</a>
 */
public interface CBSNode extends EndpointStateNotifier, Closeable {
    /**
     * Authorizes the caller with the CBS node to access resources for the {@code audience}.
     *
     * @param audience The audience to which the token applies. This can be the path within the AMQP message broker.
     * @param scopes The requested scopes for the {@link TokenCredential}.
     * @return A Mono that completes with the callee's expiration date if it is successful and errors if
     * authorization was unsuccessful. Once the expiration date has elapsed, the callee needs to reauthorize with the
     * CBS node.
     */
    Mono<OffsetDateTime> authorize(String audience, String scopes);
}
