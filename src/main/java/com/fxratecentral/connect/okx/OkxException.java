package com.fxratecentral.connect.okx;

public class OkxException extends RuntimeException {
    public OkxException(final String message) {
        super(message);
    }

    public OkxException(final Throwable throwable) {
        super(throwable);
    }
}
