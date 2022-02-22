package com.eurail.distribution.api.client.service;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created  18/12/2020
 * Copyright (c) 2020 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public class ProductProjectionSearchService extends AbstractService<ProductProjection> {
    private static final long PAGE_SIZE = 10;
    public ProductProjectionSearchService(final SphereClient client) {
        super(client);
    }

    public Collection<ProductProjection> getAll() {
        return new ArrayList<>() {{
            long page = 0;
            PagedSearchResult<ProductProjection> pagedSearchResult;
            do {
                pagedSearchResult = executeProductProjectionSearch(ProductProjectionSearch.ofCurrent().withOffset(PAGE_SIZE * page++).withLimit(PAGE_SIZE));
                addAll(pagedSearchResult.getResults());
            } while (!pagedSearchResult.isLast());
        }};
    }
}