package com.fxratecentral.connect.util;

import com.google.gson.Gson;

public final class JsonUtil {
    private final Gson gson;

    public JsonUtil() {
        this(new Gson());
    }

    public JsonUtil(final Gson gson) {
        this.gson = gson;
    }

    public String toJson(final Object object) {
        return gson.toJson(object);
    }

    public <T> T fromJson(final String json, Class<T> targetClass) {
        return gson.fromJson(json, targetClass);
    }
}
