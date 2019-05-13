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

package com.microsoft.azure.storage.blob.encryption

import com.fasterxml.jackson.databind.ObjectMapper
import com.microsoft.azure.keyvault.cryptography.SymmetricKey
import com.microsoft.azure.storage.APISpec
import com.microsoft.azure.storage.blob.BlobRange
import com.microsoft.azure.storage.blob.Metadata
import com.microsoft.rest.v2.util.FlowableUtil
import spock.lang.Unroll

import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class DecryptionTests extends APISpec {
    String keyId
    SymmetricKey symmetricKey
    BlobEncryptionPolicy blobEncryptionPolicy
    String blobName

    def setup() {
        keyId = "keyId"
        KeyGenerator keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)
        SecretKey secretKey = keyGen.generateKey()
        symmetricKey = new SymmetricKey(keyId, secretKey.getEncoded())

        blobEncryptionPolicy = new BlobEncryptionPolicy(symmetricKey, null, false)

        blobName = generateBlobName()

    }

    @Unroll
    def "Decryption"() {
        setup:
        def flow = new EncryptedFlowableForTest(testCase, symmetricKey)
        def metadata = new Metadata()
        metadata.put(Constants.ENCRYPTION_DATA_KEY, new ObjectMapper().writeValueAsString(flow.getEncryptionData()))
        def desiredOutput = flow.getPlainText().position(EncryptedFlowableForTest.DATA_OFFSET)
            .limit(EncryptedFlowableForTest.DATA_OFFSET + EncryptedFlowableForTest.DATA_COUNT)

        /*
        This BlobRange will result in an EncryptedBlobRange of 0-64. This will allow us ample room to setup ByteBuffers
        with start/end in the locations described in the docs for EncryptedFlowableForTest. The validity of variable
        range downloads is tested in EncryptedBlobAPITest, so we are ok to use constants here; here we are only testing
        how the counting and data trimming logic works.
         */
        def blobRange = new BlobRange().withOffset(EncryptedFlowableForTest.DATA_OFFSET)
            .withCount(EncryptedFlowableForTest.DATA_COUNT)

        when:
        def decryptedData = FlowableUtil.collectBytesInBuffer(
            blobEncryptionPolicy.decryptBlob(metadata, flow, new EncryptedBlobRange(blobRange), true)).blockingGet()

        then:
        decryptedData == desiredOutput

        where:
        testCase                                 | _
        EncryptedFlowableForTest.CASE_ZERO       | _
        EncryptedFlowableForTest.CASE_ONE        | _
        EncryptedFlowableForTest.CASE_TWO        | _
        EncryptedFlowableForTest.CASE_THREE      | _
        EncryptedFlowableForTest.CASE_FOUR       | _
        EncryptedFlowableForTest.CASE_FIVE       | _
        EncryptedFlowableForTest.CASE_SIX        | _
        EncryptedFlowableForTest.CASE_SEVEN      | _
        EncryptedFlowableForTest.CASE_EIGHT      | _
        EncryptedFlowableForTest.CASE_NINE       | _
        EncryptedFlowableForTest.CASE_TEN        | _
        EncryptedFlowableForTest.CASE_ELEVEN     | _
        EncryptedFlowableForTest.CASE_TWELVE     | _
        EncryptedFlowableForTest.CASE_THIRTEEN   | _
        EncryptedFlowableForTest.CASE_FOURTEEN   | _
        EncryptedFlowableForTest.CASE_FIFTEEN    | _
        EncryptedFlowableForTest.CASE_SIXTEEN    | _
        EncryptedFlowableForTest.CASE_SEVENTEEN  | _
        EncryptedFlowableForTest.CASE_EIGHTEEN   | _
        EncryptedFlowableForTest.CASE_NINETEEN   | _
        EncryptedFlowableForTest.CASE_TWENTY     | _
        EncryptedFlowableForTest.CASE_TWENTY_ONE | _
        EncryptedFlowableForTest.CASE_TWENTY_TWO | _
    }
}
