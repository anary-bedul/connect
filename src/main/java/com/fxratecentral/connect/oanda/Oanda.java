package com.fxratecentral.connect.oanda;

import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.AbstractCandlestickProvider;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.SignatureUtil;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

public class Oanda extends AbstractCandlestickProvider {
    protected Oanda(
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
        throw new OandaException("Oanda is not supported yet.");
    }
}
