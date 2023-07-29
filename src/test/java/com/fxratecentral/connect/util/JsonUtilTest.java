package com.fxratecentral.connect.util;

import com.fxratecentral.connect.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonUtilTest {
    private final JsonUtil jsonUtil = new JsonUtil();

    @Test
    public void testToJson() {
        final var object = new TestObject("value1", 2);
        final var expected = "{\"field1\":\"value1\",\"field2\":2}";
        final var actual = jsonUtil.toJson(object);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFromJson() {
        final var expected = new TestObject("value1", 2);
        final var actual = jsonUtil.fromJson("{\"field1\":\"value1\",\"field2\":2}", TestObject.class);
        Assertions.assertEquals(expected, actual);
    }
}
