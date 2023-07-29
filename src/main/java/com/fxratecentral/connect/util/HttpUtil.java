package com.fxratecentral.connect.util;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public final class HttpUtil {
    private final HttpClient httpClient;
    private final JsonUtil jsonUtil;
    private final UriUtil uriUtil;

    public HttpUtil(final UriUtil uriUtil, final JsonUtil jsonUtil) {
        this(HttpClient.newHttpClient(), uriUtil, jsonUtil);
    }

    public HttpUtil(
            final HttpClient httpClient,
            final UriUtil uriUtil,
            final JsonUtil jsonUtil) {
        this.httpClient = httpClient;
        this.jsonUtil = jsonUtil;
        this.uriUtil = uriUtil;
    }

    public <T> T get(
            final Class<T> responseClass,
            final String uriString,
            final String[] headers,
            final Map<String, Object> queryParameters) {
        final var uri = uriUtil.buildUri(uriString, queryParameters);
        final var request = HttpRequest.newBuilder(uri).headers(headers).GET().build();
        return execute(responseClass, request);
    }

    private <T> T execute(
            final Class<T> responseClass,
            final HttpRequest request) {
        try {
            final var responseHandler = HttpResponse.BodyHandlers.ofString();
            final var response = httpClient.send(request, responseHandler);
            final var responseBody = response.body();
            return jsonUtil.fromJson(responseBody, responseClass);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute.", e);
        }
    }
}
