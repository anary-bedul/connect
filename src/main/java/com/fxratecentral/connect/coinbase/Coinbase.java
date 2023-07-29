package com.fxratecentral.connect.coinbase;

import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.util.SignatureUtil;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

public final class Coinbase {
    private static final String VALUE_ACCESS_KEY = "CB_ACCESS_KEY";
    private static final String VALUE_SECRET_KEY = "CB_SECRET_KEY";
    private static final String BASE_URI = "https://api.coinbase.com";
    private static final String HEADER_CB_ACCESS_KEY = "CB-ACCESS-KEY";
    private static final String HEADER_CB_ACCESS_SIGN = "CB-ACCESS-SIGN";
    private static final String HEADER_CB_ACCESS_TIMESTAMP = "CB-ACCESS-TIMESTAMP";
    private static final String HEADER_CB_VERSION = "CB-VERSION";
    private static final String CB_VERSION = "2023-01-09";
    private static final int MAX_PRODUCT_CANDLES_PER_REQUEST = 300;
    private final HttpUtil httpUtil;
    private final SignatureUtil signatureUtil;
    private final KeyVault keyVault;

    public Coinbase(
            final HttpUtil httpUtil,
            final SignatureUtil signatureUtil,
            final KeyVault keyVault
    ) {
        this.httpUtil = httpUtil;
        this.signatureUtil = signatureUtil;
        this.keyVault = keyVault;
    }

    public Collection<Candlestick> getCandlesticks(
            final CurrencyPair currencyPair,
            final Instant startInclusive,
            final Instant endExclusive,
            final CoinbaseProductCandleGranularity granularity
    ) {
        if (startInclusive.until(endExclusive, granularity.getTemporalUnit()) > MAX_PRODUCT_CANDLES_PER_REQUEST) {
            throw new CoinbaseException("Too many product candles requested.");
        }
        final var productId = currencyPair.toString("-");
        final var productCandleResponse = getProductCandles(productId, granularity, startInclusive, endExclusive);
        return productCandleResponse
                .candles()
                .stream()
                .map(CoinbaseProductCandleConverter::convert)
                .sorted(Comparator.comparing(Candlestick::start))
                .toList();
    }

    private CoinbaseProductCandleResponse getProductCandles(
            final String productId,
            final CoinbaseProductCandleGranularity granularity,
            final Instant startInclusive,
            final Instant endExclusive) {
        final var endpoint = String.format("/v3/brokerage/products/%s/candles", productId);
        final var queryParameters = Map.<String, Object>of(
                "start", String.valueOf(startInclusive.truncatedTo(granularity.getTemporalUnit()).getEpochSecond()),
                "end", String.valueOf(endExclusive.truncatedTo(granularity.getTemporalUnit()).getEpochSecond()),
                "granularity", granularity.name()
        );
        return get(CoinbaseProductCandleResponse.class, endpoint, queryParameters);
    }

    private <T> T get(
            final Class<T> responseClass,
            final String endpoint,
            final Map<String, Object> queryParameters) {
        final var nonce = String.valueOf(Instant.now().getEpochSecond());
        final var data = nonce + "GET" + endpoint;
        final var accessKey = keyVault.get(VALUE_ACCESS_KEY);
        final var secretKey = keyVault.get(VALUE_SECRET_KEY);
        final var signature = signatureUtil.sign(secretKey, data);
        final var headers = new String[]{
                HEADER_CB_ACCESS_KEY, accessKey,
                HEADER_CB_ACCESS_SIGN, signature,
                HEADER_CB_ACCESS_TIMESTAMP, nonce,
                HEADER_CB_VERSION, CB_VERSION,
        };
        return httpUtil.get(responseClass, BASE_URI + endpoint, headers, queryParameters);
    }
}
