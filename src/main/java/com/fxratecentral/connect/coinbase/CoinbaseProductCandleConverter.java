package com.fxratecentral.connect.coinbase;

import com.fxratecentral.connect.Candlestick;
import java.math.BigDecimal;
import java.time.Instant;

public class CoinbaseProductCandleConverter {
    private CoinbaseProductCandleConverter() {
        // Non-instantiable class
    }

    public static Candlestick convert(final CoinbaseProductCandle coinbaseProductCandle) {
        return new Candlestick(
                Instant.ofEpochSecond(Long.parseLong(coinbaseProductCandle.start())),
                new BigDecimal(coinbaseProductCandle.open()),
                new BigDecimal(coinbaseProductCandle.high()),
                new BigDecimal(coinbaseProductCandle.low()),
                new BigDecimal(coinbaseProductCandle.close()),
                new BigDecimal(coinbaseProductCandle.volume())
        );
    }
}
