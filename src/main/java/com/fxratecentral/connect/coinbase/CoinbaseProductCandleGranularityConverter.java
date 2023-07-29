package com.fxratecentral.connect.coinbase;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class CoinbaseProductCandleGranularityConverter {
    private CoinbaseProductCandleGranularityConverter() {
        // Non-instantiable
    }

    public static String convert(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.MINUTES) {
            return CoinbaseProductCandleGranularity.ONE_MINUTE.name();
        }
        throw new CoinbaseException("temporalUnit is not supported: " + temporalUnit);
    }
}
