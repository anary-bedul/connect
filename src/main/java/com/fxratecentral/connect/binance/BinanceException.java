package com.fxratecentral.connect.binance;

public class BinanceException extends RuntimeException {
    public BinanceException(final String message) {
        super(message);
    }

    public BinanceException(final Throwable throwable) {
        super(throwable);
    }
}
