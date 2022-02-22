package com.eurail.distribution.api.client.executor;

import com.eurail.distribution.api.client.exception.HttpExecutorIoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created  26/12/2018 16:24
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public abstract class AbstractHttpExecutor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String DEFAULT_URL_CHARSET = StandardCharsets.UTF_8.name();
    private static final int DEFAULT_SOCKET_TIMEOUT = 60 * 1000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 60 * 1000;
    private static final int HTTP_STATUS_CODE_OK = 200;
    private static final int HTTP_STATUS_CODE_CREATED = 201;


    private String baseUrl;

    public static boolean isOk(final HttpResponse httpResponse) {
        return checkStatusCode(httpResponse, HTTP_STATUS_CODE_OK);
    }

    public static int getHttpStatusCode(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode();
    }

    public static boolean checkStatusCode(HttpResponse httpResponse, int statusCode) {
        return getHttpStatusCode(httpResponse) == statusCode;
    }

    public JsonNode getBodyAsJson(HttpResponse httpResponse) {
        try {
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), getCharset());
            return body == null || body.isEmpty() ? null : toJsonNode(body);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    public static byte[] getBodyAsBytes(HttpResponse httpResponse) {
        try {
            return IOUtils.toByteArray(httpResponse.getEntity().getContent());
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    public static String getBodyAsStringDefaultCharset(HttpResponse httpResponse) {
        try {
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), DEFAULT_URL_CHARSET);
            return body == null || body.isEmpty() ? null : body;
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected static Request addBase64Body(Request request, String body,String contentType) {
        return request.bodyString(body, ContentType.create (contentType, DEFAULT_URL_CHARSET));
    }

    public String getBodyAsString(HttpResponse httpResponse) {
        try {
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), getCharset());
            return body == null || body.isEmpty() ? null : body;
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    public static Map<String, String> getHeaders(HttpResponse httpResponse) {
        return new HashMap<>() {{
            for (final Header header : httpResponse.getAllHeaders()) {
                put(header.getName(), header.getValue());
            }
        }};
    }

    public <T> T getBodyAsObject(HttpResponse httpResponse, Class<T> clazz) {
        try {
            String body = getBodyAsString(httpResponse);
            return body == null ? null : toObject(body, clazz);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    protected int getSocketTimeout() {
        return DEFAULT_SOCKET_TIMEOUT;
    }

    protected int getConnectTimeout() {
        return DEFAULT_CONNECT_TIMEOUT;
    }

    protected void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getBaseUrl() {
        if (baseUrl == null) {
            throw new UnsupportedOperationException("Base Url is not set");
        }
        return baseUrl;
    }

    protected HttpResponse execGet(String url) {
        return execGet(url, Collections.emptyMap(), Collections.emptyMap());
    }

    protected HttpResponse execGet(String url, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(Request.Get(createRequestUrl(url, parameters)), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execDelete(String url, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(Request.Delete(createRequestUrl(url, parameters)), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected <T> HttpResponse execPut(String url, T body, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(addBody(Request.Put(createRequestUrl(url, parameters)), body), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execPut(String url, JsonNode node, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(addBody(Request.Put(createRequestUrl(url, parameters)), convertToString(node)), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execPut(String url, Map<String, Object> body, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(addBody(Request.Put(createRequestUrl(url, parameters)), body), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execPost(String url, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(Request.Post(createRequestUrl(url, parameters)), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execPost(String url, String body, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(addBody(Request.Post(createRequestUrl(url, parameters)), body), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execPost(String url, Map<String, Object> body, Map<String, Object> headers, Map<String, Object> parameters) {
        try {
            return execute(addBody(Request.Post(createRequestUrl(url, parameters)), body), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected <T> HttpResponse execPost(String url, T body, Map<String, Object> headers, Map<String, Object> parameters)  {
        try {
            return execute(addBody(Request.Post(createRequestUrl(url, parameters)), body), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected <T> HttpResponse execPost(String url, String body, Map<String, Object> headers, Map<String, Object> parameters, String contentType) {
        try {
            return execute(addBase64Body(Request.Post(createRequestUrl(url, parameters)), body, contentType), headers);
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected HttpResponse execute(Request request, Map<String, Object> headers) {
        try {
            return addHeaders(request, headers)
                    .socketTimeout(getSocketTimeout())
                    .connectTimeout(getConnectTimeout())
                    .execute().returnResponse();
        } catch (IOException e) {
            throw new HttpExecutorIoException(e);
        }
    }

    protected static Request addHeaders(Request request, Map<String, Object> headers) {
        if (headers != null && headers.size() != 0) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        return request;
    }

    protected <T> Request addBody(Request request, T body) {
        return request.bodyString(convertToString(body), ContentType.APPLICATION_JSON);
    }

    protected Request addBody(Request request, Map<String, Object> body) {
        return request.bodyString(convertToString(body), ContentType.APPLICATION_JSON);
    }

    protected static Request addBody(Request request, String body) {
        return request.bodyString(body, ContentType.APPLICATION_JSON);
    }

    protected String getCharset() {
        return DEFAULT_URL_CHARSET;
    }

    protected String addParameters(String url, Map<String, Object> parameters) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        if (parameters != null && parameters.size() != 0) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                sb
                        .append(entry.getKey())
                        .append("=")
                        .append(encodeRequestParameter(entry.getValue()))
                        .append("&");

            }
        }
        return sb.subSequence(0, sb.length() - 1).toString();
    }


    protected String createRequestUrl(String url, Map<String, Object> parameters) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder(getBaseUrl());
        if (!url.startsWith("/")) {
            sb.append("/");
        }
        sb.append(addParameters(url, parameters));
        return sb.toString();
    }

    protected String encodeRequestParameter(Object parameter) throws UnsupportedEncodingException {
        return parameter == null ? "" : URLEncoder.encode(parameter.toString(), getCharset());
    }

    protected String convertToString(Map<String, Object> body) {
        return toJsonNode(body).toString();
    }

    protected <T> String convertToString(T body) {
        return toJsonNode(body).toString();
    }

    protected JsonNode toJsonNode(Object object) {
        return getObjectMapper().valueToTree(object);
    }

    protected JsonNode toJsonNode(String string) throws IOException {
        return getObjectMapper().readTree(string);
    }

    protected <T> T toObject(final String string, final Class<T> clazz) throws IOException {
        return getObjectMapper().readValue(string, clazz);
    }

}
