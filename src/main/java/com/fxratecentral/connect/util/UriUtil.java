package com.fxratecentral.connect.util;

import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

public final class UriUtil {
    public URI buildUri(final String uriString, final Map<String, Object> queryParameters) {
        final var uriBuilder = UriBuilder.fromUri(uriString);
        for (final var queryParameter : queryParameters.entrySet()) {
            uriBuilder.queryParam(queryParameter.getKey(), queryParameter.getValue());
        }
        return uriBuilder.build();
    }
}
