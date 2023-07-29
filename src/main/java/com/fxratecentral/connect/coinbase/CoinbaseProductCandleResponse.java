package com.fxratecentral.connect.coinbase;

import java.util.Collection;

public record CoinbaseProductCandleResponse(Collection<CoinbaseProductCandle> candles) {
    // Empty
}
