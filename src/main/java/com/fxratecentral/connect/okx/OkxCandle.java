package com.fxratecentral.connect.okx;

public record OkxCandle(Long openTime, String openPrice, String highPrice, String lowPrice,
    String closePrice, String volume, String currencyVolume, String quoteCurrencyVolume,
    int comfirm) {
    // Empty.
}
