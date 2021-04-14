package com.eurail.distribution.api.client.service;

import com.eurail.distribution.api.client.exception.CommerceToolsClientException;
import com.eurail.distribution.api.client.exception.InvalidObjectValueException;
import com.eurail.distribution.api.client.exception.NotSingleValueException;
import io.sphere.sdk.client.ConcurrentModificationException;
import io.sphere.sdk.client.InvalidTokenException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.commands.Command;
import io.sphere.sdk.models.ResourceView;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.List;

/**
 * Created  01/05/2018 09:00
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public abstract class AbstractService<T extends ResourceView> {
    private final SphereClient client;

    protected AbstractService(SphereClient client) {
        this.client = client;
    }

    /**
     * Executes request.
     *
     *  When you read single object typically this is reading by primary key, or by unique key (sql analog).
     *  Some "keys" supported by ct (for example "ReadById", "readByContainerAndKey").
     *  In this case executeRequest(...) must be invoked (because not possible to get more then one object)
     *  This object must be present, otherwise  NotFoundException will be thrown.
     *
     * @see #executeRequestNullable(SphereRequest<T>)
     *
     * @param sphereRequest request
     * @return result object
     * @throws NotFoundException if no result found
     */
    protected T executeRequest(final SphereRequest<T> sphereRequest) {
        return executeRequest(sphereRequest, true);
    }

    /**
     * Executes request.
     * @see  #executeRequest(SphereRequest<T>).
     *
     *  Deference is that object can be absent, and null will be returned.
     *
     * @param sphereRequest request
     * @return single object or null of object not found
     */
    protected T executeRequestNullable(final SphereRequest<T> sphereRequest) {
        return executeRequest(sphereRequest, false);
    }

    /**
     * Executes query
     *
     *  This method invoked when you expect collection (even empty collection).
     *  Typically when extracted Custom Object and ct does not have appropriate method and predicate used.
     *  If only one(single) object expected use
     *  @see #getSingleObject(SphereRequest<PagedQueryResult<T>>)
     *  or
     *  @see #getSingleObjectNullable(SphereRequest<PagedQueryResult<T>>)
     *
     * @param sphereRequest request
     * @return page of objects
     */
    protected PagedQueryResult<T> executeQuery(final SphereRequest<PagedQueryResult<T>> sphereRequest) {
        try {
            return client.execute(sphereRequest).toCompletableFuture().get();
        } catch (Exception e) {
            throw new CommerceToolsClientException(e);
        }
    }

    /**
     * Executes query
     *
     *  This method invoked when you expect collection (even empty collection).
     *  Typically when extracted Custom Object and ct does not have appropriate method and predicate used.
     *  If only one(single) object expected use
     *  @see #getSingleObject(SphereRequest<PagedQueryResult<T>>)
     *  or
     *  @see #getSingleObjectNullable(SphereRequest<PagedQueryResult<T>>)
     *
     * @param sphereRequest request
     * @return list of objects
     */
    protected List<T> executeQueryList(final SphereRequest<List<T>> sphereRequest) {
        try {
            return client.execute(sphereRequest).toCompletableFuture().get();
        } catch (Exception e) {
            throw new CommerceToolsClientException(e);
        }
    }

    /**
     * Executes custom request.
     * Can be used as extension for CommerceTools
     *
     *
     * @param command Custom command
     * @return String that should be converted to appropriate object
     */
    protected <C extends Command<String>> String executeCustomCommandRequest(final C command) {
        return client.execute(command).toCompletableFuture().join();
    }

    /**
     * Executes product projection search.
     * Used in validator.
     *
     * @param productProjectionSearch product projection search request
     * @return page or search results
     */
    protected PagedSearchResult<ProductProjection> executeProductProjectionSearch(final ProductProjectionSearch productProjectionSearch) {
        try {
            return client.execute(productProjectionSearch).toCompletableFuture().get();
        } catch (Exception e) {
            throw new CommerceToolsClientException(e);
        }
    }

    /**
     * Returns single object.
     *
     * @param sphereRequest request
     * @return single object
     * @throws InvalidObjectValueException if total number of elements not provided
     * @throws NotSingleValueException if more then one result
     * @throws NotFoundException if no result found
     */
    protected T getSingleObject(final SphereRequest<PagedQueryResult<T>> sphereRequest) {
        return getSingleObject(sphereRequest, true);
    }

    /**
     * Returns single object optional.
     *
     * @param sphereRequest request
     * @return single object ot null if not found.
     * @throws InvalidObjectValueException if total number of elements not provided
     * @throws NotSingleValueException if more then one result
     */
    protected T getSingleObjectNullable(final SphereRequest<PagedQueryResult<T>> sphereRequest) {
        return getSingleObject(sphereRequest, false);
    }

    private T getSingleObject(final SphereRequest<PagedQueryResult<T>> sphereRequest, boolean throwIfEmpty) {
        final PagedQueryResult<T> pagedQueryResult = executeQuery(sphereRequest);

        final Long total = getTotal(pagedQueryResult);

        if (total == 0) {
            if (throwIfEmpty) {
                throw new NotFoundException();
            }
            return null;
        }

        if (total > 1) {
            throw new NotSingleValueException("%d objects found", total);
        }

        return pagedQueryResult.getResults().iterator().next();
    }

    private T executeRequest(final SphereRequest<T> sphereRequest, boolean throwIfNull) {
        try {
            final T result = client.execute(sphereRequest).toCompletableFuture().get();
            if (result == null && throwIfNull) {
                throw new NotFoundException();
            }
            return result;
        } catch (NotFoundException | ConcurrentModificationException | InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new CommerceToolsClientException(e);
        }
    }

    private static Long getTotal(final PagedQueryResult pagedQueryResult) {
        final Long result = pagedQueryResult.getTotal();
        if (result == null) {
            throw new InvalidObjectValueException("Value 'total' not provided for PagedQueryResult");
        }
        return result;
    }
}

