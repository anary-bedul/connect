package com.fxratecentral.connect.coinbase;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public final class CoinbaseProductCandleGranularityConverter {
    private CoinbaseProductCandleGranularityConverter() {
        // Non-instantiable class
    }

    public static String convert(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.MINUTES) {
            return CoinbaseProductCandleGranularity.ONE_MINUTE.name();
        }
        throw new CoinbaseException("Granularity is not supported: " + temporalUnit);
    }
}
