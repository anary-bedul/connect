package com.fxratecentral.connect.okx;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.LinkedHashMap;
import com.fxratecentral.connect.AbstractCandlestickProvider;
import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.SignatureUtil;

public class Okx extends AbstractCandlestickProvider {
    private static final String BASE_URI = "https://www.okx.com";
    private static final int MAX_ROW_PER_REQUEST = 300;


    protected Okx(final HttpUtil httpUtil, final SignatureUtil signatureUtil,
        final KeyVault keyVault) {
        super(httpUtil, signatureUtil, keyVault);
    }

    public Collection<Candlestick> getCandlesticksHistory(final CurrencyPair currencyPair,
        final Instant startInclusive, final Instant endExclusive,
        final TemporalUnit temporalUnit) {

        final var symbol = currencyPair.toString("-");
        final var interval = OkxCandleIntervalConverter.convert(temporalUnit);
        final var startTime = String.valueOf(startInclusive.toEpochMilli());
        final var endTime = String.valueOf(endExclusive.minus(1, temporalUnit).toEpochMilli());
        Collection<OkxCandle> okxCandle = getCandles(symbol, interval, startTime, endTime,
            MAX_ROW_PER_REQUEST, "/api/v5/market/history-candles");
        return okxCandle.stream().map(OkxCandleConverter::convert).toList();
    }

    @Override
    public Collection<Candlestick> getCandlesticks(final CurrencyPair currencyPair,
        final Instant startInclusive, final Instant endExclusive,
        final TemporalUnit temporalUnit) {
        final var symbol = currencyPair.toString("-");
        final var interval = OkxCandleIntervalConverter.convert(temporalUnit);
        final var startTime = String.valueOf(startInclusive.toEpochMilli());
        final var endTime = String.valueOf(endExclusive.minus(1, temporalUnit).toEpochMilli());
        Collection<OkxCandle> okxCandle = getCandles(symbol, interval, startTime, endTime,
            MAX_ROW_PER_REQUEST, "/api/v5/market/candles");
        return okxCandle.stream().map(OkxCandleConverter::convert).toList();
    }

    private Collection<OkxCandle> getCandles(final String symbol, final String interval,
        final String startTime, final String endTime, final int limit, final String endpoint) {
        final var queryParameters = new LinkedHashMap<String, Object>();
        queryParameters.put("instId", symbol);
        queryParameters.put("bar", interval);
        queryParameters.put("after", endTime);
        queryParameters.put("before", startTime);
        return get(OkxResponseClass.class, endpoint, queryParameters, false).getData().stream()
        .map(OkxCandleConverter::convert).toList();
    }

    private <T> OkxResponseClass get(final Class<T> responseClass, final String endpoint,
        final LinkedHashMap<String, Object> queryParameters, final boolean sign) {
        final var headers = new String[] {"Accept", "*/*"};

        OkxResponseClass okxResp = (OkxResponseClass) httpUtil.get(responseClass,
            BASE_URI + endpoint, headers, queryParameters);
        if (okxResp != null && !okxResp.getCode().equals("0")) {
            throw new OkxException(String.format("Error Code = %s, Message = %s", okxResp.getCode(),
            okxResp.getMsg()));

        }
        return okxResp;
    }
}
