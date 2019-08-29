// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.models;

import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static com.azure.storage.common.Constants.EncryptionConstants;
import static com.azure.storage.common.Constants.EncryptionConstants.ENCRYPTION_BLOCK_SIZE;

/**
 * Represents a blob encryption policy that is used to perform envelope encryption/decryption of Azure storage blobs.
 * This class is immutable as it is a property on an EncryptedBlobURL, which needs to be thread safe.
 */
public final class BlobEncryptionPolicy {
    private final ClientLogger logger = new ClientLogger(BlobEncryptionPolicy.class);


    /**
     * The {@link IKeyResolver} used to select the correct key for decrypting existing blobs.
     */
    private final IKeyResolver keyResolver;

    /**
     * An object of type {@link IKey} that is used to wrap/unwrap the content key during encryption.
     */
    private final IKey keyWrapper;

    /**
     * A value to indicate that the data read from the server should be encrypted.
     */
    private final boolean requireEncryption;

    /**
     * Initializes a new instance of the {@link BlobEncryptionPolicy} class with the specified key and resolver.
     * <p>
     * If the generated policy is intended to be used for encryption, users are expected to provide a key at the
     * minimum. The absence of key will cause an exception to be thrown during encryption. If the generated policy is
     * intended to be used for decryption, users can provide a keyResolver. The client library will - 1. Invoke the key
     * resolver if specified to get the key. 2. If resolver is not specified but a key is specified, match the key id on
     * the key and use it.
     *
     * @param key An object of type {@link IKey} that is used to wrap/unwrap the content encryption key.
     * @param keyResolver  The key resolver used to select the correct key for decrypting existing blobs.
     * @param requireEncryption If set to true, decryptBlob() will throw an IllegalArgumentException if blob is not encrypted.
     */
    public BlobEncryptionPolicy(IKey key, IKeyResolver keyResolver, boolean requireEncryption) {
        this.keyWrapper = key;
        this.keyResolver = keyResolver;
        this.requireEncryption = requireEncryption;
    }

    /**
     * Encrypts the given Flux ByteBuffer.
     *
     * @param plainTextFlux The Flux ByteBuffer to be encrypted.
     *
     * @return A {@link EncryptedBlob}
     *
     * @throws InvalidKeyException
     */
    Mono<EncryptedBlob> encryptBlob(Flux<ByteBuffer> plainTextFlux) throws InvalidKeyException {
        Objects.requireNonNull(this.keyWrapper);
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);

            Cipher cipher = Cipher.getInstance(EncryptionConstants.AES_CBC_PKCS5PADDING);

            // Generate content encryption key
            SecretKey aesKey = keyGen.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

            Map<String, String> keyWrappingMetadata = new HashMap<>();
            keyWrappingMetadata.put(EncryptionConstants.AGENT_METADATA_KEY, EncryptionConstants.AGENT_METADATA_VALUE);

            return this.keyWrapper.wrapKeyAsync(aesKey.getEncoded(), null /* algorithm */)
                .map(encryptedKey -> {
                    WrappedKey wrappedKey = new WrappedKey(
                        this.keyWrapper.getKid(), encryptedKey.getT1(), encryptedKey.getT2());

                    // Build EncryptionData
                    EncryptionData encryptionData = new EncryptionData()
                        .withEncryptionMode(EncryptionConstants.ENCRYPTION_MODE)
                        .withEncryptionAgent(
                            new EncryptionAgent(EncryptionConstants.ENCRYPTION_PROTOCOL_V1,
                                EncryptionAlgorithm.AES_CBC_256))
                        .withKeyWrappingMetadata(keyWrappingMetadata)
                        .withContentEncryptionIV(cipher.getIV())
                        .withWrappedContentKey(wrappedKey);

                    // Encrypt plain text with content encryption key
                    Flux<ByteBuffer> encryptedTextFlux = plainTextFlux.map(plainTextBuffer -> {
                        int outputSize = cipher.getOutputSize(plainTextBuffer.remaining());

                        /*
                        This should be the only place we allocate memory in encryptBlob(). Although there is an
                        overload that can encrypt in place that would save allocations, we do not want to overwrite
                        customer's memory, so we must allocate our own memory. If memory usage becomes unreasonable,
                        we should implement pooling.
                         */
                        ByteBuffer encryptedTextBuffer = ByteBuffer.allocate(outputSize);

                        int encryptedBytes;
                        try {
                            encryptedBytes = cipher.update(plainTextBuffer, encryptedTextBuffer);
                        } catch (ShortBufferException e) {
                            throw logger.logExceptionAsError(Exceptions.propagate(e));
                        }
                        encryptedTextBuffer.position(0);
                        encryptedTextBuffer.limit(encryptedBytes);
                        return encryptedTextBuffer;
                    });

                    /*
                    Defer() ensures the contained code is not executed until the Flux is subscribed to, in
                    other words, cipher.doFinal() will not be called until the plainTextFlux has completed
                    and therefore all other data has been encrypted.
                     */
                    encryptedTextFlux = Flux.concat(encryptedTextFlux, Flux.defer(() -> {
                        try {
                            return Flux.just(ByteBuffer.wrap(cipher.doFinal()));
                        } catch (GeneralSecurityException e) {
                            throw logger.logExceptionAsError(Exceptions.propagate(e));
                        }
                    }));
                    return new EncryptedBlob(encryptionData, encryptedTextFlux);
                });
        }
        // These are hardcoded and guaranteed to work. There is no reason to propogate a checked exception.
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypted all or part of an encrypted Block-, Page- or AppendBlob.
     *
     * @param metadata The Blob's {@link com.azure.storage.blob.models.Metadata}
     * @param encryptedFlux The encrypted Flux of ByteBuffer to decrypt
     * @param encryptedBlobRange A {@link EncryptedBlobRange} indicating the range to decrypt
     * @param padding Boolean indicating if the padding mode should be set or not.
     *
     * @return A Flux ByteBuffer that has been decrypted
     */
    Flux<ByteBuffer> decryptBlob(Map<String, String> metadata, Flux<ByteBuffer> encryptedFlux,
        EncryptedBlobRange encryptedBlobRange, boolean padding) {
        if (this.keyWrapper == null && this.keyResolver == null) {
            throw new IllegalArgumentException("Key and KeyResolver cannot both be null");
        }

        EncryptionData encryptionData = getAndValidateEncryptionData(metadata);
        if (encryptionData == null) {
            if (this.requireEncryption) {
                throw new IllegalStateException("Require encryption is set to true but the blob is not encrypted.");
            } else {
                return encryptedFlux;
            }
        }


        // The number of bytes we have put into the Cipher so far.
        AtomicLong totalInputBytes = new AtomicLong(0);
        // The number of bytes that have been sent to the downstream so far.
        AtomicLong totalOutputBytes = new AtomicLong(0);

        return getKeyEncryptionKey(encryptionData)
            .flatMapMany(contentEncryptionKey -> {

                /*
                Calculate the IV.

                If we are starting at the beginning, we can grab the IV from the encryptionData. Otherwise,
                Rx makes it difficult to grab the first 16 bytes of data to pass as an IV to the cipher.
                As a work around, we initialize the cipher with a garbage IV (empty byte array) and attempt to
                decrypt the first 16 bytes (the actual IV for the relevant data). We throw away this "decrypted"
                data. Now, though, because each block of 16 is used as the IV for the next, the original 16 bytes
                of downloaded data are in position to be used as the IV for the data actually requested and we are
                in the desired state.
                 */
                byte[] IV = new byte[ENCRYPTION_BLOCK_SIZE];
                /*
                Adjusting the range by <= 16 means we only adjusted to align on an encryption block boundary
                (padding will add 1-16 bytes as it will prefer to pad 16 bytes instead of 0) and therefore the key
                is in the metadata.
                 */
                if (encryptedBlobRange.offsetAdjustment() <= ENCRYPTION_BLOCK_SIZE) {
                    IV = encryptionData.contentEncryptionIV();
                }

                Cipher cipher;
                try {
                    cipher = getCipher(contentEncryptionKey, encryptionData, IV, padding);
                } catch (InvalidKeyException e) {
                    throw logger.logExceptionAsError(Exceptions.propagate(e));
                }

                return encryptedFlux.map(encryptedByteBuffer -> {
                    ByteBuffer plaintextByteBuffer;
                    /*
                    If we could potentially decrypt more bytes than encryptedByteBuffer can hold, allocate more
                    room. Note that, because getOutputSize returns the size needed to store
                    max(updateOutputSize, finalizeOutputSize), this is likely to produce a ByteBuffer slightly
                    larger than what the real outputSize is. This is accounted for below.
                     */
                    int outputSize = cipher.getOutputSize(encryptedByteBuffer.remaining());
                    if (outputSize > encryptedByteBuffer.remaining()) {
                        plaintextByteBuffer = ByteBuffer.allocate(outputSize);
                    } else {
                        /*
                        This is an ok optimization on download as the underlying buffer is not the customer's but
                        the protocol layer's, which does not expect to be able to reuse it.
                         */
                        plaintextByteBuffer = encryptedByteBuffer.duplicate();
                    }

                    // First, determine if we should update or finalize and fill the output buffer.

                    // We will have reached the end of the downloaded range. Finalize.
                    int decryptedBytes;
                    int bytesToInput = encryptedByteBuffer.remaining();
                    if (totalInputBytes.longValue() + bytesToInput >= encryptedBlobRange.adjustedDownloadCount()) {
                        try {
                            decryptedBytes = cipher.doFinal(encryptedByteBuffer, plaintextByteBuffer);
                        } catch (GeneralSecurityException e) {
                            throw logger.logExceptionAsError(Exceptions.propagate(e));
                        }
                    }
                    // We will not have reached the end of the downloaded range. Update.
                    else {
                        try {
                            decryptedBytes = cipher.update(encryptedByteBuffer, plaintextByteBuffer);
                        } catch (ShortBufferException e) {
                            throw logger.logExceptionAsError(Exceptions.propagate(e));
                        }
                    }
                    totalInputBytes.addAndGet(bytesToInput);
                    plaintextByteBuffer.position(0); // Reset the position after writing.

                    // Next, determine and set the position of the output buffer.

                    /*
                    The amount of data sent so far has not yet reached customer-requested data. i.e. it starts
                    somewhere in either the IV or the range adjustment to align on a block boundary. We should
                    advance the position so the customer does not read this data.
                     */
                    if (totalOutputBytes.longValue() <= encryptedBlobRange.offsetAdjustment()) {
                        /*
                        Note that the cast is safe because of the bounds on offsetAdjustment (see encryptedBlobRange
                        for details), which here upper bounds totalInputBytes.
                        Note that we do not simply set the position to be offsetAdjustment because of the (unlikely)
                        case that some ByteBuffers were small enough to be entirely contained within the
                        offsetAdjustment, so when we do reach customer-requested data, advancing the position by
                        the whole offsetAdjustment would be too much.
                         */
                        int remainingAdjustment = encryptedBlobRange.offsetAdjustment() -
                            (int) totalOutputBytes.longValue();

                        /*
                        Setting the position past the limit will throw. This is in the case of very small
                        ByteBuffers that are entirely contained within the offsetAdjustment.
                         */
                        int newPosition = remainingAdjustment <= plaintextByteBuffer.limit() ?
                            remainingAdjustment : plaintextByteBuffer.limit();

                        plaintextByteBuffer.position(newPosition);

                    }
                    /*
                    Else: The beginning of this ByteBuffer is somewhere after the offset adjustment. If it is in the
                    middle of customer requested data, let it be. If it starts in the end adjustment, this will
                    be trimmed effectively by setting the limit below.
                     */

                    // Finally, determine and set the limit of the output buffer.

                    long beginningOfEndAdjustment; // read: beginning of end-adjustment.
                    /*
                    The user intended to download the whole blob, so the only extra we downloaded was padding, which
                    is trimmed by the cipher automatically; there is effectively no beginning to the end-adjustment.
                     */
                    if (encryptedBlobRange.originalRange().count() == null) {
                        beginningOfEndAdjustment = Long.MAX_VALUE;
                    }
                    // Calculate the end of the user-requested data so we can trim anything after.
                    else {
                        beginningOfEndAdjustment = encryptedBlobRange.offsetAdjustment() +
                            encryptedBlobRange.originalRange().count();
                    }

                    /*
                    The end of the Cipher output lies after customer requested data (in the end adjustment) and
                    should be trimmed.
                     */
                    if (decryptedBytes + totalOutputBytes.longValue() > beginningOfEndAdjustment) {
                        long amountPastEnd // past the end of user-requested data.
                            = decryptedBytes + totalOutputBytes.longValue() - beginningOfEndAdjustment;
                        /*
                        Note that amountPastEnd can only be up to 16, so the cast is safe. We do not need to worry
                        about limit() throwing because we allocated at least enough space for decryptedBytes and
                        the newLimit will be less than that. In the case where this Cipher output starts after the
                        beginning of the endAdjustment, we don't want to send anything back, so we set limit to be
                        the same as position.
                         */
                        int newLimit = totalOutputBytes.longValue() <= beginningOfEndAdjustment ?
                            decryptedBytes - (int) amountPastEnd : plaintextByteBuffer.position();
                        plaintextByteBuffer.limit(newLimit);
                    }
                    /*
                    The end of this Cipher output is before the end adjustment and after the offset adjustment, so
                    it will lie somewhere in customer requested data. It is possible we allocated a ByteBuffer that
                    is slightly too large, so we set the limit equal to exactly the amount we decrypted to be safe.
                     */
                    else if (decryptedBytes + totalOutputBytes.longValue() >
                        encryptedBlobRange.offsetAdjustment()) {
                        plaintextByteBuffer.limit(decryptedBytes);
                    }
                    /*
                    Else: The end of this ByteBuffer will not reach the beginning of customer-requested data. Make
                    it effectively empty.
                     */
                    else {
                        plaintextByteBuffer.limit(plaintextByteBuffer.position());
                    }

                    totalOutputBytes.addAndGet(decryptedBytes);
                    return plaintextByteBuffer;
                });
            });
    }

    /**
     * Creates a new {@link BlobEncryptionPolicy} with given keyWrapper.
     *
     * @param keyWrapper A {@link IKey} to set.
     *
     * @return {@link BlobEncryptionPolicy}
     */
    public BlobEncryptionPolicy withKeyWrapper(IKey keyWrapper) {
        return new BlobEncryptionPolicy(keyWrapper, this.keyResolver, this.requireEncryption);
    }

    /**
     * Creates a new {@link BlobEncryptionPolicy} with given keyResolver.
     *
     * @param keyResolver A {@link IKeyResolver} to set.
     *
     * @return {@link BlobEncryptionPolicy}
     */
    public BlobEncryptionPolicy withKeyResolver(IKeyResolver keyResolver) {
        return new BlobEncryptionPolicy(this.keyWrapper, keyResolver, this.requireEncryption);
    }

    /**
     * Creates a new {@link BlobEncryptionPolicy} with given requireEncryption.
     *
     * @param requireEncryption If {@code true}, all data uploaded will be encrypted, and any downloads that are not
     *         encrypted will raise an exception.
     *
     * @return {@link BlobEncryptionPolicy}
     */
    public BlobEncryptionPolicy withRequireEncryption(boolean requireEncryption) {
        return new BlobEncryptionPolicy(this.keyWrapper, this.keyResolver, requireEncryption);
    }

    /**
     * Gets and validates {@link EncryptionData} from a Blob's metadata
     *
     * @param metadata {@code Map} of String -> String
     *
     * @return {@link EncryptionData}
     */
    private EncryptionData getAndValidateEncryptionData(Map<String, String> metadata) {
        Objects.requireNonNull(metadata);
        String encryptedDataString = metadata.get(EncryptionConstants.ENCRYPTION_DATA_KEY);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EncryptionData encryptionData = objectMapper.readValue(encryptedDataString, EncryptionData.class);

            if (encryptionData == null) {
                if (this.requireEncryption) {
                    throw new IllegalArgumentException(
                        "Encryption data does not exist. If you do not want to decrypt the data, please do not " +
                            "set the require encryption flag to true");
                } else {
                    return null;
                }
            }

            Objects.requireNonNull(encryptionData.contentEncryptionIV());
            Objects.requireNonNull(encryptionData.wrappedContentKey().encryptedKey());

            // Throw if the encryption protocol on the message doesn't match the version that this client library
            // understands
            // and is able to decrypt.
            if (!EncryptionConstants.ENCRYPTION_PROTOCOL_V1.equals(encryptionData.encryptionAgent().protocol())) {
                throw logger.logExceptionAsError(new IllegalArgumentException(String.format(Locale.ROOT,
                    "Invalid Encryption Agent. This version of the client library does not understand the " +
                        "Encryption Agent set on the queue message: %s",
                    encryptionData.encryptionAgent())));
            }
            return encryptionData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the key encryption key for blob. First tries to get key encryption key from KeyResolver, then
     * falls back to IKey stored on this EncryptionPolicy.
     *
     * @param encryptionData A {@link EncryptionData}
     *
     * @return Key encryption key as a byte array
     */
    private Mono<byte[]> getKeyEncryptionKey(EncryptionData encryptionData) {
        /*
        1. Invoke the key resolver if specified to get the key. If the resolver is specified but does not have a
        mapping for the key id, an error should be thrown. This is important for key rotation scenario.
        2. If resolver is not specified but a key is specified, match the key id on the key and and use it.
        */

        Mono<IKey> keyMono;

        if (this.keyResolver != null) {
            keyMono = this.keyResolver.resolveKeyAsync(encryptionData.wrappedContentKey().keyId())
                .onErrorResume(NullPointerException.class, e -> {
                    /*
                    keyResolver returns null if it cannot find the key, but RX throws on null values
                    passing through workflows, so we propagate this case with an IllegalArgumentException
                     */
                    throw logger.logExceptionAsError(Exceptions.propagate(e));
                });
        } else {
            if (encryptionData.wrappedContentKey().keyId().equals(this.keyWrapper.getKid())) {
                keyMono = Mono.just(this.keyWrapper);
            } else {
                throw logger.logExceptionAsError(new IllegalArgumentException("Key mismatch. The key id stored on " +
                    "the service does not match the specified key."));
            }
        }

        return keyMono.flatMap(keyEncryptionKey -> {
            try {
                return this.keyWrapper.unwrapKeyAsync(
                    encryptionData.wrappedContentKey().encryptedKey(),
                    encryptionData.wrappedContentKey().algorithm()
                );
            } catch (NoSuchAlgorithmException e) {
                throw logger.logExceptionAsError(Exceptions.propagate(e));
            }
        });
    }

    /**
     * Creates a {@link Cipher} using given content encryption key, encryption data, iv, and padding.
     *
     * @param contentEncryptionKey The content encryption key, used to decrypt the contents of the blob.
     * @param encryptionData {@link EncryptionData}
     * @param iv IV used to initialize the Cipher.  If IV is null, encryptionData
     * @param padding If cipher should use padding. Padding is necessary to decrypt all the way to end of a blob.
     *         Otherwise, don't use padding.
     *
     * @return {@link Cipher}
     *
     * @throws InvalidKeyException
     */
    private Cipher getCipher(byte[] contentEncryptionKey, EncryptionData encryptionData, byte[] iv, boolean padding)
        throws InvalidKeyException {
        try {
            switch (encryptionData.encryptionAgent().algorithm()) {
                case AES_CBC_256:
                    Cipher cipher;
                    if (padding) {
                        cipher = Cipher.getInstance(EncryptionConstants.AES_CBC_PKCS5PADDING);
                    } else {
                        cipher = Cipher.getInstance(EncryptionConstants.AES_CBC_NO_PADDING);
                    }
                    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                    SecretKey keySpec = new SecretKeySpec(contentEncryptionKey, 0, contentEncryptionKey.length,
                        EncryptionConstants.AES);
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
                    return cipher;
                default:
                    throw logger.logExceptionAsError(new IllegalArgumentException(
                        "Invalid Encryption Algorithm found on the resource. This version of the client library " +
                            "does not support the specified encryption algorithm."));
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw logger.logExceptionAsError(Exceptions.propagate(e));
        }
    }
}
