package com.fxratecentral.connect;

public record CurrencyPair(String baseCurrencyCode, String quoteCurrencyCode) {
    @Override
    public String toString() {
        return toString("/");
    }

    public String toString(final String separator) {
        return baseCurrencyCode + separator + quoteCurrencyCode;
    }
}
