package com.fxratecentral.connect.binance;

import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CandlestickProvider;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.SignatureUtil;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

public final class Binance extends CandlestickProvider {
    public Binance(
            final HttpUtil httpUtil,
            final SignatureUtil signatureUtil,
            final KeyVault keyVault
    ) {
        super(httpUtil, signatureUtil, keyVault);
    }

    @Override
    public Collection<Candlestick> getCandlesticks(
            final CurrencyPair currencyPair,
            final Instant startInclusive,
            final Instant endExclusive,
            final TemporalUnit temporalUnit
    ) {
        throw new BinanceException("Binance is not supported yet.");
    }
}
