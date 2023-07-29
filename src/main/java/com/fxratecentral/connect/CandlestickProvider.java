package com.fxratecentral.connect;

import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.SignatureUtil;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

public abstract class CandlestickProvider {
    protected final HttpUtil httpUtil;
    protected final SignatureUtil signatureUtil;
    protected final KeyVault keyVault;

    protected CandlestickProvider(
            final HttpUtil httpUtil,
            final SignatureUtil signatureUtil,
            final KeyVault keyVault
    ) {
        this.httpUtil = httpUtil;
        this.signatureUtil = signatureUtil;
        this.keyVault = keyVault;
    }

    public abstract Collection<Candlestick> getCandlesticks(
            final CurrencyPair currencyPair,
            final Instant startInclusive,
            final Instant endExclusive,
            final TemporalUnit temporalUnit
    );
}
