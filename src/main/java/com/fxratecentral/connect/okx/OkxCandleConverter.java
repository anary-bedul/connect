package com.fxratecentral.connect.okx;

import com.fxratecentral.connect.Candlestick;
import java.math.BigDecimal;
import java.time.Instant;

public final class OkxCandleConverter {
    private OkxCandleConverter() {
        // Non-instantiable class
    }

    public static Candlestick convert(final OkxCandle okxCandle) {
        return new Candlestick(Instant.ofEpochMilli(okxCandle.openTime()),
            new BigDecimal(okxCandle.openPrice()), new BigDecimal(okxCandle.highPrice()),
            new BigDecimal(okxCandle.lowPrice()), new BigDecimal(okxCandle.closePrice()),
            new BigDecimal(okxCandle.volume()));
    }

    public static OkxCandle convert(final Object object) {
        Object[] rawObject = (Object[]) object;
        Double openTime = Double.valueOf((String) rawObject[0]);
        return new OkxCandle(BigDecimal.valueOf(openTime).longValue(), (String) rawObject[1],
                (String) rawObject[2], (String) rawObject[3], (String) rawObject[4],
                (String) rawObject[5], (String) rawObject[6], (String) rawObject[7],
                Integer.parseInt((String) rawObject[8]));
    }
}
