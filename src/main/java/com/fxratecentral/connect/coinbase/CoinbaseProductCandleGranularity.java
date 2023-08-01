package com.fxratecentral.connect.coinbase;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum CoinbaseProductCandleGranularity {
    ONE_MINUTE(ChronoUnit.MINUTES, 1);

    private final TemporalUnit temporalUnit;
    private final long temporalQuantity;

    CoinbaseProductCandleGranularity(final TemporalUnit temporalUnit, final long temporalQuantity) {
        this.temporalUnit = temporalUnit;
        this.temporalQuantity = temporalQuantity;
    }

    public TemporalUnit getTemporalUnit() {
        return temporalUnit;
    }

    public long getTemporalQuantity() {
        return temporalQuantity;
    }
}
