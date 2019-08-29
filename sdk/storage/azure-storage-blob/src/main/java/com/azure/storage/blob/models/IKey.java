/*
 * Copyright Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azure.storage.blob.models;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.security.NoSuchAlgorithmException;

/**
 * TODO (rickle-msft): Remove in favor of keyvault core track 2
 */
public interface IKey {

    /**
     * The unique key identifier for this key.
     *
     * @return The key identifier
     */
    String getKid();

    /**
     * Wraps (encrypts) the specified symmetric key material using the specified
     * algorithm, or the keys DefaultKeyWrapAlgorithm if none is specified.
     *
     * @param key
     *            The symmetric key to wrap
     * @param algorithm
     *            The wrapping algorithm to use, defaults to the keys
     *            DefaultKeyWrapAlgorithm
     * @return ListenableFuture containing the encrypted key and the algorithm
     *         that was used
     * @throws NoSuchAlgorithmException the algorithm is not valid
     */
    Mono<Tuple2<byte[], String>> wrapKeyAsync(final byte[] key, final String algorithm) throws NoSuchAlgorithmException;

    /**
     * Unwraps (decrypts) the specified encryped key material.
     *
     * @param encryptedKey
     *            The encrypted key to decrypt
     * @param algorithm
     *            The algorithm to use, must be supplied
     * @return A ListenableFuture containing the unwrapped key
     * @throws NoSuchAlgorithmException the algorithm is not valid
     */
    Mono<byte[]> unwrapKeyAsync(final byte[] encryptedKey, final String algorithm) throws NoSuchAlgorithmException;
}
