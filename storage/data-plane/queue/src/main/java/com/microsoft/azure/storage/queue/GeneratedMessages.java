/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 * <p>
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package com.microsoft.azure.storage.queue;

import com.microsoft.azure.storage.queue.models.DequeuedMessageItem;
import com.microsoft.azure.storage.queue.models.EnqueuedMessage;
import com.microsoft.azure.storage.queue.models.MessageClearResponse;
import com.microsoft.azure.storage.queue.models.MessageDequeueResponse;
import com.microsoft.azure.storage.queue.models.MessageEnqueueResponse;
import com.microsoft.azure.storage.queue.models.MessagePeekResponse;
import com.microsoft.azure.storage.queue.models.PeekedMessageItem;
import com.microsoft.azure.storage.queue.models.QueueMessage;
import com.microsoft.azure.storage.queue.models.StorageErrorException;
import com.microsoft.rest.v2.Context;
import com.microsoft.rest.v2.RestProxy;
import com.microsoft.rest.v2.ServiceCallback;
import com.microsoft.rest.v2.ServiceFuture;
import com.microsoft.rest.v2.Validator;
import com.microsoft.rest.v2.annotations.BodyParam;
import com.microsoft.rest.v2.annotations.DELETE;
import com.microsoft.rest.v2.annotations.ExpectedResponses;
import com.microsoft.rest.v2.annotations.GET;
import com.microsoft.rest.v2.annotations.HeaderParam;
import com.microsoft.rest.v2.annotations.Host;
import com.microsoft.rest.v2.annotations.HostParam;
import com.microsoft.rest.v2.annotations.POST;
import com.microsoft.rest.v2.annotations.QueryParam;
import com.microsoft.rest.v2.annotations.UnexpectedResponseExceptionType;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

import java.util.List;

/**
 * An instance of this class provides access to all the operations defined in
 * GeneratedMessages.
 */
public final class GeneratedMessages {
    /**
     * The proxy service used to perform REST calls.
     */
    private MessagesService service;

    /**
     * The service client containing this operation class.
     */
    private GeneratedStorageClient client;

    /**
     * Initializes an instance of GeneratedMessages.
     *
     * @param client
     *         the instance of the service client containing this operation class.
     */
    public GeneratedMessages(GeneratedStorageClient client) {
        this.service = RestProxy.create(MessagesService.class, client);
        this.client = client;
    }

    /**
     * The interface defining all the services for GeneratedMessages to be used
     * by the proxy service to perform REST calls.
     */
    @Host("{url}")
    private interface MessagesService {
        @GET("{queueName}/messages")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(StorageErrorException.class)
        Single<MessageDequeueResponse> dequeue(Context context, @HostParam("url") String url, @QueryParam("numofmessages") Integer numberOfMessages, @QueryParam("visibilitytimeout") Integer visibilitytimeout, @QueryParam("timeout") Integer timeout, @HeaderParam("x-ms-version") String version, @HeaderParam("x-ms-client-request-id") String requestId);

        @DELETE("{queueName}/messages")
        @ExpectedResponses({204})
        @UnexpectedResponseExceptionType(StorageErrorException.class)
        Single<MessageClearResponse> clear(Context context, @HostParam("url") String url, @QueryParam("timeout") Integer timeout, @HeaderParam("x-ms-version") String version, @HeaderParam("x-ms-client-request-id") String requestId);

        @POST("{queueName}/messages")
        @ExpectedResponses({201})
        @UnexpectedResponseExceptionType(StorageErrorException.class)
        Single<MessageEnqueueResponse> enqueue(Context context, @HostParam("url") String url, @BodyParam("application/xml; charset=utf-8") QueueMessage queueMessage, @QueryParam("visibilitytimeout") Integer visibilitytimeout, @QueryParam("messagettl") Integer messageTimeToLive, @QueryParam("timeout") Integer timeout, @HeaderParam("x-ms-version") String version, @HeaderParam("x-ms-client-request-id") String requestId);

        @GET("{queueName}/messages")
        @ExpectedResponses({200})
        @UnexpectedResponseExceptionType(StorageErrorException.class)
        Single<MessagePeekResponse> peek(Context context, @HostParam("url") String url, @QueryParam("numofmessages") Integer numberOfMessages, @QueryParam("timeout") Integer timeout, @HeaderParam("x-ms-version") String version, @HeaderParam("x-ms-client-request-id") String requestId, @QueryParam("peekonly") String peekonly);
    }

    /**
     * The Dequeue operation retrieves one or more messages from the front of the queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return the List&lt;DequeuedMessageItem&gt; object if successful.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     * @throws StorageErrorException
     *         thrown if the request is rejected by server.
     * @throws RuntimeException
     *         all other wrapped checked exceptions if the request fails to be sent.
     */
    public List<DequeuedMessageItem> dequeue(Context context, Integer numberOfMessages, Integer visibilitytimeout, Integer timeout, String requestId) {
        return dequeueAsync(context, numberOfMessages, visibilitytimeout, timeout, requestId).blockingGet();
    }

    /**
     * The Dequeue operation retrieves one or more messages from the front of the queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     * @param serviceCallback
     *         the async ServiceCallback to handle successful and failed responses.
     *
     * @return a ServiceFuture which will be completed with the result of the network request.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public ServiceFuture<List<DequeuedMessageItem>> dequeueAsync(Context context, Integer numberOfMessages, Integer visibilitytimeout, Integer timeout, String requestId, ServiceCallback<List<DequeuedMessageItem>> serviceCallback) {
        return ServiceFuture.fromBody(dequeueAsync(context, numberOfMessages, visibilitytimeout, timeout, requestId), serviceCallback);
    }

    /**
     * The Dequeue operation retrieves one or more messages from the front of the queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Single<MessageDequeueResponse> dequeueWithRestResponseAsync(Context context, Integer numberOfMessages, Integer visibilitytimeout, Integer timeout, String requestId) {
        if (this.client.url() == null) {
            throw new IllegalArgumentException("Parameter this.client.url() is required and cannot be null.");
        }
        if (this.client.version() == null) {
            throw new IllegalArgumentException("Parameter this.client.version() is required and cannot be null.");
        }
        return service.dequeue(context, this.client.url(), numberOfMessages, visibilitytimeout, timeout, this.client.version(), requestId);
    }

    /**
     * The Dequeue operation retrieves one or more messages from the front of the queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Maybe<List<DequeuedMessageItem>> dequeueAsync(Context context, Integer numberOfMessages, Integer visibilitytimeout, Integer timeout, String requestId) {
        return dequeueWithRestResponseAsync(context, numberOfMessages, visibilitytimeout, timeout, requestId)
                .flatMapMaybe((MessageDequeueResponse res) -> res.body() == null ? Maybe.empty() : Maybe.just(res.body()));
    }

    /**
     * The Clear operation deletes all messages from the specified queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     * @throws StorageErrorException
     *         thrown if the request is rejected by server.
     * @throws RuntimeException
     *         all other wrapped checked exceptions if the request fails to be sent.
     */
    public void clear(Context context, Integer timeout, String requestId) {
        clearAsync(context, timeout, requestId).blockingAwait();
    }

    /**
     * The Clear operation deletes all messages from the specified queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     * @param serviceCallback
     *         the async ServiceCallback to handle successful and failed responses.
     *
     * @return a ServiceFuture which will be completed with the result of the network request.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public ServiceFuture<Void> clearAsync(Context context, Integer timeout, String requestId, ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromBody(clearAsync(context, timeout, requestId), serviceCallback);
    }

    /**
     * The Clear operation deletes all messages from the specified queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Single<MessageClearResponse> clearWithRestResponseAsync(Context context, Integer timeout, String requestId) {
        if (this.client.url() == null) {
            throw new IllegalArgumentException("Parameter this.client.url() is required and cannot be null.");
        }
        if (this.client.version() == null) {
            throw new IllegalArgumentException("Parameter this.client.version() is required and cannot be null.");
        }
        return service.clear(context, this.client.url(), timeout, this.client.version(), requestId);
    }

    /**
     * The Clear operation deletes all messages from the specified queue.
     *
     * @param context
     *         The context to associate with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Completable clearAsync(Context context, Integer timeout, String requestId) {
        return clearWithRestResponseAsync(context, timeout, requestId)
                .toCompletable();
    }

    /**
     * The Enqueue operation adds a new message to the back of the message queue. A visibility timeout can also be specified to make the message invisible until the visibility timeout expires. A message must be in a format that can be included in an XML request with UTF-8 encoding. The encoded message can be up to 64 KB in size for versions 2011-08-18 and newer, or 8 KB in size for previous versions.
     *
     * @param context
     *         The context to associate with this operation.
     * @param queueMessage
     *         A Message object which can be stored in a Queue.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param messageTimeToLive
     *         Optional. Specifies the time-to-live interval for the message, in seconds. Prior to version 2017-07-29, the maximum time-to-live allowed is 7 days. For version 2017-07-29 or later, the maximum time-to-live can be any positive number, as well as -1 indicating that the message does not expire. If this parameter is omitted, the default time-to-live is 7 days.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return the List&lt;EnqueuedMessage&gt; object if successful.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     * @throws StorageErrorException
     *         thrown if the request is rejected by server.
     * @throws RuntimeException
     *         all other wrapped checked exceptions if the request fails to be sent.
     */
    public List<EnqueuedMessage> enqueue(Context context, @NonNull QueueMessage queueMessage, Integer visibilitytimeout, Integer messageTimeToLive, Integer timeout, String requestId) {
        return enqueueAsync(context, queueMessage, visibilitytimeout, messageTimeToLive, timeout, requestId).blockingGet();
    }

    /**
     * The Enqueue operation adds a new message to the back of the message queue. A visibility timeout can also be specified to make the message invisible until the visibility timeout expires. A message must be in a format that can be included in an XML request with UTF-8 encoding. The encoded message can be up to 64 KB in size for versions 2011-08-18 and newer, or 8 KB in size for previous versions.
     *
     * @param context
     *         The context to associate with this operation.
     * @param queueMessage
     *         A Message object which can be stored in a Queue.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param messageTimeToLive
     *         Optional. Specifies the time-to-live interval for the message, in seconds. Prior to version 2017-07-29, the maximum time-to-live allowed is 7 days. For version 2017-07-29 or later, the maximum time-to-live can be any positive number, as well as -1 indicating that the message does not expire. If this parameter is omitted, the default time-to-live is 7 days.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     * @param serviceCallback
     *         the async ServiceCallback to handle successful and failed responses.
     *
     * @return a ServiceFuture which will be completed with the result of the network request.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public ServiceFuture<List<EnqueuedMessage>> enqueueAsync(Context context, @NonNull QueueMessage queueMessage, Integer visibilitytimeout, Integer messageTimeToLive, Integer timeout, String requestId, ServiceCallback<List<EnqueuedMessage>> serviceCallback) {
        return ServiceFuture.fromBody(enqueueAsync(context, queueMessage, visibilitytimeout, messageTimeToLive, timeout, requestId), serviceCallback);
    }

    /**
     * The Enqueue operation adds a new message to the back of the message queue. A visibility timeout can also be specified to make the message invisible until the visibility timeout expires. A message must be in a format that can be included in an XML request with UTF-8 encoding. The encoded message can be up to 64 KB in size for versions 2011-08-18 and newer, or 8 KB in size for previous versions.
     *
     * @param context
     *         The context to associate with this operation.
     * @param queueMessage
     *         A Message object which can be stored in a Queue.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param messageTimeToLive
     *         Optional. Specifies the time-to-live interval for the message, in seconds. Prior to version 2017-07-29, the maximum time-to-live allowed is 7 days. For version 2017-07-29 or later, the maximum time-to-live can be any positive number, as well as -1 indicating that the message does not expire. If this parameter is omitted, the default time-to-live is 7 days.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Single<MessageEnqueueResponse> enqueueWithRestResponseAsync(Context context, @NonNull QueueMessage queueMessage, Integer visibilitytimeout, Integer messageTimeToLive, Integer timeout, String requestId) {
        if (this.client.url() == null) {
            throw new IllegalArgumentException("Parameter this.client.url() is required and cannot be null.");
        }
        if (queueMessage == null) {
            throw new IllegalArgumentException("Parameter queueMessage is required and cannot be null.");
        }
        if (this.client.version() == null) {
            throw new IllegalArgumentException("Parameter this.client.version() is required and cannot be null.");
        }
        Validator.validate(queueMessage);
        return service.enqueue(context, this.client.url(), queueMessage, visibilitytimeout, messageTimeToLive, timeout, this.client.version(), requestId);
    }

    /**
     * The Enqueue operation adds a new message to the back of the message queue. A visibility timeout can also be specified to make the message invisible until the visibility timeout expires. A message must be in a format that can be included in an XML request with UTF-8 encoding. The encoded message can be up to 64 KB in size for versions 2011-08-18 and newer, or 8 KB in size for previous versions.
     *
     * @param context
     *         The context to associate with this operation.
     * @param queueMessage
     *         A Message object which can be stored in a Queue.
     * @param visibilitytimeout
     *         Optional. Specifies the new visibility timeout value, in seconds, relative to server time. The default value is 30 seconds. A specified value must be larger than or equal to 1 second, and cannot be larger than 7 days, or larger than 2 hours on REST protocol versions prior to version 2011-08-18. The visibility timeout of a message can be set to a value later than the expiry time.
     * @param messageTimeToLive
     *         Optional. Specifies the time-to-live interval for the message, in seconds. Prior to version 2017-07-29, the maximum time-to-live allowed is 7 days. For version 2017-07-29 or later, the maximum time-to-live can be any positive number, as well as -1 indicating that the message does not expire. If this parameter is omitted, the default time-to-live is 7 days.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Maybe<List<EnqueuedMessage>> enqueueAsync(Context context, @NonNull QueueMessage queueMessage, Integer visibilitytimeout, Integer messageTimeToLive, Integer timeout, String requestId) {
        return enqueueWithRestResponseAsync(context, queueMessage, visibilitytimeout, messageTimeToLive, timeout, requestId)
                .flatMapMaybe((MessageEnqueueResponse res) -> res.body() == null ? Maybe.empty() : Maybe.just(res.body()));
    }

    /**
     * The Peek operation retrieves one or more messages from the front of the queue, but does not alter the visibility of the message.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return the List&lt;PeekedMessageItem&gt; object if successful.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     * @throws StorageErrorException
     *         thrown if the request is rejected by server.
     * @throws RuntimeException
     *         all other wrapped checked exceptions if the request fails to be sent.
     */
    public List<PeekedMessageItem> peek(Context context, Integer numberOfMessages, Integer timeout, String requestId) {
        return peekAsync(context, numberOfMessages, timeout, requestId).blockingGet();
    }

    /**
     * The Peek operation retrieves one or more messages from the front of the queue, but does not alter the visibility of the message.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     * @param serviceCallback
     *         the async ServiceCallback to handle successful and failed responses.
     *
     * @return a ServiceFuture which will be completed with the result of the network request.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public ServiceFuture<List<PeekedMessageItem>> peekAsync(Context context, Integer numberOfMessages, Integer timeout, String requestId, ServiceCallback<List<PeekedMessageItem>> serviceCallback) {
        return ServiceFuture.fromBody(peekAsync(context, numberOfMessages, timeout, requestId), serviceCallback);
    }

    /**
     * The Peek operation retrieves one or more messages from the front of the queue, but does not alter the visibility of the message.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Single<MessagePeekResponse> peekWithRestResponseAsync(Context context, Integer numberOfMessages, Integer timeout, String requestId) {
        if (this.client.url() == null) {
            throw new IllegalArgumentException("Parameter this.client.url() is required and cannot be null.");
        }
        if (this.client.version() == null) {
            throw new IllegalArgumentException("Parameter this.client.version() is required and cannot be null.");
        }
        final String peekonly = "true";
        return service.peek(context, this.client.url(), numberOfMessages, timeout, this.client.version(), requestId, peekonly);
    }

    /**
     * The Peek operation retrieves one or more messages from the front of the queue, but does not alter the visibility of the message.
     *
     * @param context
     *         The context to associate with this operation.
     * @param numberOfMessages
     *         Optional. A nonzero integer value that specifies the number of messages to retrieve from the queue, up to a maximum of 32. If fewer are visible, the visible messages are returned. By default, a single message is retrieved from the queue with this operation.
     * @param timeout
     *         The The timeout parameter is expressed in seconds. For more information, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-queue-service-operations&gt;Setting Timeouts for Queue Service Operations.&lt;/a&gt;.
     * @param requestId
     *         Provides a client-generated, opaque value with a 1 KB character limit that is recorded in the analytics logs when storage analytics logging is enabled.
     *
     * @return a Single which performs the network request upon subscription.
     *
     * @throws IllegalArgumentException
     *         thrown if parameters fail the validation.
     */
    public Maybe<List<PeekedMessageItem>> peekAsync(Context context, Integer numberOfMessages, Integer timeout, String requestId) {
        return peekWithRestResponseAsync(context, numberOfMessages, timeout, requestId)
                .flatMapMaybe((MessagePeekResponse res) -> res.body() == null ? Maybe.empty() : Maybe.just(res.body()));
    }
}
