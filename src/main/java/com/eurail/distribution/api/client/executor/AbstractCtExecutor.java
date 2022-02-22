package com.eurail.distribution.api.client.executor;

import com.eurail.distribution.api.client.exception.HttpExecutorIoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sphere.sdk.json.SphereJsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created  2019-08-19 11:25
 * Copyright (c) 2019 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public abstract class AbstractCtExecutor extends AbstractHttpExecutor {
    private static final ObjectMapper CT_OBJECT_MAPPER = SphereJsonUtils.newObjectMapper();
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";
    private static final Map<String, String> HEADERS = Map.of("Content-Type", MediaType.APPLICATION_JSON);
    private final String project;
    private final String accessToken;

    protected abstract String getPrefix();

    protected AbstractCtExecutor(final String baseUrl, final String project, final String accessToken) {
        setBaseUrl(baseUrl);
        this.project = project;
        this.accessToken = accessToken;
    }

    protected ObjectMapper getObjectMapper() {
        return CT_OBJECT_MAPPER;
    }

    protected String buildUrl() {
        return getProject() +  "/" + getPrefix();
    }

    protected Map<String, String> getDefaultHeaders() {
        return HEADERS;
    }

    protected String getAccessToken() {
        return accessToken;
    }

    protected String getProject() {
        return project;
    }

    protected Map<String, Object> createHeaders() {
        return new HashMap<>(getDefaultHeaders()){{
            put(AUTHORIZATION, BEARER + " " + getAccessToken());
        }};
    }

    protected HttpResponse execPost(Map<String, Object> headers, Map<String, List<Object>> parameters, String url, String body) {
        try {
            return execute(addBody(Request.Post(createRequestUrl(parameters, url)), body), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execGet(Map<String, Object> headers, Map<String, List<Object>> parameters, String url) {
        try {
            return execute(Request.Get(createRequestUrl(parameters, url)), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected String createRequestUrl(Map<String, List<Object>> parameters, String url) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder(getBaseUrl());
        if (!url.startsWith("/")) {
            sb.append("/");
        }
        sb.append(addParameters(parameters, url));
        return sb.toString();
    }

    protected String addParameters(Map<String, List<Object>> parameters, String url) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        if (parameters != null && parameters.size() != 0) {
            for (Map.Entry<String, List<Object>> entry : parameters.entrySet()) {
                List<Object> queryParamList = entry.getValue();
                for (Object param : queryParamList) {
                    sb
                            .append(entry.getKey())
                            .append("=")
                            .append(encodeRequestParameter(param))
                            .append("&");
                }
            }
        }
        return sb.subSequence(0, sb.length() - 1).toString();
    }
}