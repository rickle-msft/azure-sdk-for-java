// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.authorization.fluent;

import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.http.rest.PagedFlux;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.resourcemanager.authorization.fluent.models.MicrosoftGraphServicePrincipalInner;
import com.azure.resourcemanager.authorization.fluent.models.ServicePrincipalsServicePrincipalExpand;
import com.azure.resourcemanager.authorization.fluent.models.ServicePrincipalsServicePrincipalOrderby;
import com.azure.resourcemanager.authorization.fluent.models.ServicePrincipalsServicePrincipalSelect;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * An instance of this class provides access to all the operations defined in ServicePrincipalsServicePrincipalsClient.
 */
public interface ServicePrincipalsServicePrincipalsClient {
    /**
     * Get entities from servicePrincipals.
     *
     * @param consistencyLevel Indicates the requested consistency level.
     * @param top Show only the first n items.
     * @param skip Skip the first n items.
     * @param search Search items by search phrases.
     * @param filter Filter items by property values.
     * @param count Include count of items.
     * @param orderby Order items by property values.
     * @param select Select properties to be returned.
     * @param expand Expand related entities.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entities from servicePrincipals.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    PagedFlux<MicrosoftGraphServicePrincipalInner> listServicePrincipalAsync(
        String consistencyLevel,
        Integer top,
        Integer skip,
        String search,
        String filter,
        Boolean count,
        List<ServicePrincipalsServicePrincipalOrderby> orderby,
        List<ServicePrincipalsServicePrincipalSelect> select,
        List<ServicePrincipalsServicePrincipalExpand> expand);

    /**
     * Get entities from servicePrincipals.
     *
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entities from servicePrincipals.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    PagedFlux<MicrosoftGraphServicePrincipalInner> listServicePrincipalAsync();

    /**
     * Get entities from servicePrincipals.
     *
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entities from servicePrincipals.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    PagedIterable<MicrosoftGraphServicePrincipalInner> listServicePrincipal();

    /**
     * Get entities from servicePrincipals.
     *
     * @param consistencyLevel Indicates the requested consistency level.
     * @param top Show only the first n items.
     * @param skip Skip the first n items.
     * @param search Search items by search phrases.
     * @param filter Filter items by property values.
     * @param count Include count of items.
     * @param orderby Order items by property values.
     * @param select Select properties to be returned.
     * @param expand Expand related entities.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entities from servicePrincipals.
     */
    @ServiceMethod(returns = ReturnType.COLLECTION)
    PagedIterable<MicrosoftGraphServicePrincipalInner> listServicePrincipal(
        String consistencyLevel,
        Integer top,
        Integer skip,
        String search,
        String filter,
        Boolean count,
        List<ServicePrincipalsServicePrincipalOrderby> orderby,
        List<ServicePrincipalsServicePrincipalSelect> select,
        List<ServicePrincipalsServicePrincipalExpand> expand,
        Context context);

    /**
     * Add new entity to servicePrincipals.
     *
     * @param body New entity.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return represents an Azure Active Directory object.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Response<MicrosoftGraphServicePrincipalInner>> createServicePrincipalWithResponseAsync(
        MicrosoftGraphServicePrincipalInner body);

    /**
     * Add new entity to servicePrincipals.
     *
     * @param body New entity.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return represents an Azure Active Directory object.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<MicrosoftGraphServicePrincipalInner> createServicePrincipalAsync(MicrosoftGraphServicePrincipalInner body);

    /**
     * Add new entity to servicePrincipals.
     *
     * @param body New entity.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return represents an Azure Active Directory object.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    MicrosoftGraphServicePrincipalInner createServicePrincipal(MicrosoftGraphServicePrincipalInner body);

    /**
     * Add new entity to servicePrincipals.
     *
     * @param body New entity.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return represents an Azure Active Directory object.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Response<MicrosoftGraphServicePrincipalInner> createServicePrincipalWithResponse(
        MicrosoftGraphServicePrincipalInner body, Context context);

    /**
     * Get entity from servicePrincipals by key.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param consistencyLevel Indicates the requested consistency level.
     * @param select Select properties to be returned.
     * @param expand Expand related entities.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entity from servicePrincipals by key.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Response<MicrosoftGraphServicePrincipalInner>> getServicePrincipalWithResponseAsync(
        String servicePrincipalId,
        String consistencyLevel,
        List<ServicePrincipalsServicePrincipalSelect> select,
        List<ServicePrincipalsServicePrincipalExpand> expand);

    /**
     * Get entity from servicePrincipals by key.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param consistencyLevel Indicates the requested consistency level.
     * @param select Select properties to be returned.
     * @param expand Expand related entities.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entity from servicePrincipals by key.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<MicrosoftGraphServicePrincipalInner> getServicePrincipalAsync(
        String servicePrincipalId,
        String consistencyLevel,
        List<ServicePrincipalsServicePrincipalSelect> select,
        List<ServicePrincipalsServicePrincipalExpand> expand);

    /**
     * Get entity from servicePrincipals by key.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entity from servicePrincipals by key.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<MicrosoftGraphServicePrincipalInner> getServicePrincipalAsync(String servicePrincipalId);

    /**
     * Get entity from servicePrincipals by key.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entity from servicePrincipals by key.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    MicrosoftGraphServicePrincipalInner getServicePrincipal(String servicePrincipalId);

    /**
     * Get entity from servicePrincipals by key.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param consistencyLevel Indicates the requested consistency level.
     * @param select Select properties to be returned.
     * @param expand Expand related entities.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return entity from servicePrincipals by key.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Response<MicrosoftGraphServicePrincipalInner> getServicePrincipalWithResponse(
        String servicePrincipalId,
        String consistencyLevel,
        List<ServicePrincipalsServicePrincipalSelect> select,
        List<ServicePrincipalsServicePrincipalExpand> expand,
        Context context);

    /**
     * Update entity in servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param body New property values.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the completion.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Response<Void>> updateServicePrincipalWithResponseAsync(
        String servicePrincipalId, MicrosoftGraphServicePrincipalInner body);

    /**
     * Update entity in servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param body New property values.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the completion.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Void> updateServicePrincipalAsync(String servicePrincipalId, MicrosoftGraphServicePrincipalInner body);

    /**
     * Update entity in servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param body New property values.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    void updateServicePrincipal(String servicePrincipalId, MicrosoftGraphServicePrincipalInner body);

    /**
     * Update entity in servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param body New property values.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Response<Void> updateServicePrincipalWithResponse(
        String servicePrincipalId, MicrosoftGraphServicePrincipalInner body, Context context);

    /**
     * Delete entity from servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param ifMatch ETag.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the completion.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Response<Void>> deleteServicePrincipalWithResponseAsync(String servicePrincipalId, String ifMatch);

    /**
     * Delete entity from servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param ifMatch ETag.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the completion.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Void> deleteServicePrincipalAsync(String servicePrincipalId, String ifMatch);

    /**
     * Delete entity from servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the completion.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Mono<Void> deleteServicePrincipalAsync(String servicePrincipalId);

    /**
     * Delete entity from servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    void deleteServicePrincipal(String servicePrincipalId);

    /**
     * Delete entity from servicePrincipals.
     *
     * @param servicePrincipalId key: id of servicePrincipal.
     * @param ifMatch ETag.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.resourcemanager.authorization.fluent.models.OdataErrorMainException thrown if the request is
     *     rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    Response<Void> deleteServicePrincipalWithResponse(String servicePrincipalId, String ifMatch, Context context);
}
