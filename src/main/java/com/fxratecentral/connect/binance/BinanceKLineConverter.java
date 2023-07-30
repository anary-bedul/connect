package com.fxratecentral.connect.binance;

import com.fxratecentral.connect.Candlestick;
import java.math.BigDecimal;
import java.time.Instant;

public final class BinanceKLineConverter {
    private BinanceKLineConverter() {
        // Non-instantiable class
    }

    public static Candlestick convert(final BinanceKLine kline) {
        return new Candlestick(
            Instant.ofEpochMilli(kline.openTime()),
            new BigDecimal(kline.openPrice()),
            new BigDecimal(kline.highPrice()),
            new BigDecimal(kline.lowPrice()),
            new BigDecimal(kline.closePrice()),
            new BigDecimal(kline.volume())
        );
    }

    public static BinanceKLine convert(final Object[] rawObject) {
        return new BinanceKLine(
            BigDecimal.valueOf((Double) rawObject[0]).longValue(),
            (String) rawObject[1],
            (String) rawObject[2],
            (String) rawObject[3],
            (String) rawObject[4],
            (String) rawObject[5],
            BigDecimal.valueOf((Double) rawObject[6]).longValue(),
            (String) rawObject[7],
            BigDecimal.valueOf((Double) rawObject[8]).longValue(),
            (String) rawObject[9],
            (String) rawObject[10]
            // unusedFieldIgnore object[11];
        );
    }
}
