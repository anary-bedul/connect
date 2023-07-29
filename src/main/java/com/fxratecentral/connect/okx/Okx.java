package com.fxratecentral.connect.okx;

import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CandlestickProvider;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.SignatureUtil;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;

public class Okx extends CandlestickProvider {
    protected Okx(
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
        throw new OkxException("OKX is not supported yet.");
    }
}
