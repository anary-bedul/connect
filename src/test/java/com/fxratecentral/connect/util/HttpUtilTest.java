package com.fxratecentral.connect.util;

import com.fxratecentral.connect.TestObject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpUtilTest {
    private HttpUtil httpUtil;
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<Object> mockResponse;

    @BeforeEach
    public void setup() {
        final UriUtil uriUtil = new UriUtil();
        final JsonUtil jsonUtil = new JsonUtil();
        httpUtil = new HttpUtil(httpClient, uriUtil, jsonUtil);
    }

    @Test
    public void testGet() throws IOException, InterruptedException {
        when(httpClient.send(any(), any())).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn("{\"field1\":\"abc\",\"field2\":12345}");
        final var url = "https://www.test.com";
        final var headers = new String[] {"testKey", "testValue"};
        final var queryParameters = Map.<String, Object>of();
        final var response = httpUtil.get(TestObject.class, url, headers, queryParameters);
        Assertions.assertEquals("abc", response.field1());
        Assertions.assertEquals(12345, response.field2());
    }
}
