package com.eurail.distribution.api.client.executor;

import org.apache.http.HttpResponse;

import java.util.List;
import java.util.Map;

/**
 * Created  2019-11-27 00:03
 * Copyright (c) 2019 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public class GetProductProjectionSearchCtExecutor extends AbstractCtExecutor {
    private static final String PREFIX = "product-projections/search";

    public GetProductProjectionSearchCtExecutor(final String baseUrl, final String project, final String accessToken) {
        super(baseUrl, project, accessToken);
    }

    public HttpResponse exec(final Map<String, List<Object>> queryParameters) {
        return execGet(createHeaders(), queryParameters, buildUrl());
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }
}
