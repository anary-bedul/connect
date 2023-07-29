package com.fxratecentral.connect;

import java.util.Objects;

public class TestObject {
    public String field1;
    public Integer field2;

    public TestObject() {
        // Default no-arg constructor needed for JSON parsing
    }

    public TestObject(final String field1, final Integer field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof final TestObject cast)) {
            return false;
        }
        if (!Objects.equals(this.field1, cast.field1)) {
            return false;
        }
        if (!Objects.equals(this.field2, cast.field2)) {
            return false;
        }
        return true;
    }
}
