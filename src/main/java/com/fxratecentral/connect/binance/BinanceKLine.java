package com.fxratecentral.connect.binance;

public record BinanceKLine(
    Long openTime,
    String openPrice,
    String highPrice,
    String lowPrice,
    String closePrice,
    String volume,
    Long closeTime,
    String quoteAssetVolume,
    Long numberOfTrades,
    String takerBuyBaseAssetVolume,
    String takerBuyQuoteAssetVolume
) {
    // Empty.
}
