package com.fxratecentral.connect.coinbase;

public record CoinbaseProductCandle(
    String start,
    String open,
    String high,
    String low,
    String close,
    String volume
) {
    // Empty.
}
