package com.fxratecentral.connect.binance;

import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CandlestickProvider;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.SignatureUtil;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

public final class Binance extends CandlestickProvider {
    private static final String VALUE_ACCESS_KEY = "BNNC_ACCESS_KEY";
    private static final String VALUE_SECRET_KEY = "BNNC_SECRET_KEY";
    private static final String BASE_URI = "https://api.binance.com";
    private static final String HEADER_BNNC_ACCESS_KEY = "X-MBX-APIKEY";
    private static final int MAX_KLINES_PER_REQUEST = 1000;

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
        if (startInclusive.until(endExclusive, temporalUnit) > MAX_KLINES_PER_REQUEST) {
            throw new BinanceException("Too many klines requested.");
        }
        final var symbol = currencyPair.toString("");
        final var interval = BinanceKLineIntervalConverter.convert(temporalUnit);
        final var startTime = String.valueOf(startInclusive.toEpochMilli());
        final var endTime = String.valueOf(endExclusive.minus(1, temporalUnit).toEpochMilli());
        return getKLines(symbol, interval, startTime, endTime, MAX_KLINES_PER_REQUEST)
                .stream()
                .map(BinanceKLineConverter::convert)
                .toList();
    }

    /**
     * @see <a href="https://binance-docs.github.io/apidocs/spot/en/#kline-candlestick-data">kline-candlestick-data</a>
     */
    private Collection<BinanceKLine> getKLines(
            final String symbol,
            final String interval,
            final String startTime,
            final String endTime,
            final int limit
    ) {
        final var endpoint = "/api/v3/klines";
        final var queryParameters = new LinkedHashMap<String, Object>();
        queryParameters.put("symbol", symbol);
        queryParameters.put("interval", interval);
        queryParameters.put("startTime", startTime);
        queryParameters.put("endTime", endTime);
        queryParameters.put("limit", limit);
        return Arrays.stream(get(Object[][].class, endpoint, queryParameters, false))
                .map(BinanceKLineConverter::convert)
                .toList();
    }

    private <T> T get(
            final Class<T> responseClass,
            final String endpoint,
            final LinkedHashMap<String, Object> queryParameters,
            final boolean sign
    ) {
        final var nonce = String.valueOf(Instant.now().toEpochMilli());
        final var data = "timestamp=" + nonce;
        final var accessKey = keyVault.get(VALUE_ACCESS_KEY);
        final var secretKey = keyVault.get(VALUE_SECRET_KEY);
        final var signature = signatureUtil.sign(secretKey, data);
        final var headers = new String[]{
                HEADER_BNNC_ACCESS_KEY, accessKey,
        };
        if (sign) {
            queryParameters.put("timestamp", nonce);
            queryParameters.put("signature", signature);
        }
        return httpUtil.get(responseClass, BASE_URI + endpoint, headers, queryParameters);
    }
}
