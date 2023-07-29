package com.fxratecentral.connect;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

public interface CandlestickProvider {
    Collection<Candlestick> getCandlesticks(
            final CurrencyPair currencyPair,
            final Instant startInclusive,
            final Instant endExclusive,
            final TemporalUnit temporalUnit
    );
}
