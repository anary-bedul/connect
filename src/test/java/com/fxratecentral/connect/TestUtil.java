package com.fxratecentral.connect;

import com.fxratecentral.connect.util.JsonUtil;
import java.io.IOException;

public final class TestUtil {
    private TestUtil() {
        // Utility class
    }

    public static <T> T readJsonResource(
        final JsonUtil jsonUtil,
        final String resourceFilename,
        final Class<T> responseClass
    ) {
        try (var inputStream = TestUtil.class.getClassLoader().getResourceAsStream(resourceFilename)) {
            final var rawJson = new String(inputStream.readAllBytes());
            return jsonUtil.fromJson(rawJson, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
