package com.fxratecentral.connect.util;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriUtilTest {
    private UriUtil uriUtil;

    @BeforeEach
    public void setup() {
        uriUtil = new UriUtil();
    }

    @Test
    public void testBuildUriWithQueryParameters() {
        final var endpoint = "https://www.example.com/test";
        final var queryParameters = new LinkedHashMap<String, Object>();
        queryParameters.put("field1", "value1");
        queryParameters.put("field2", 2);
        final var expected = endpoint + "?field1=value1&field2=2";
        final var actual = uriUtil.buildUri(endpoint, queryParameters);
        assertEquals(expected, actual.toString());
    }
}
