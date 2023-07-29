package com.fxratecentral.connect.coinbase;

import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.TestUtil;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.JsonUtil;
import com.fxratecentral.connect.util.SignatureUtil;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoinbaseTest {
    private final JsonUtil jsonUtil = new JsonUtil();
    @Mock
    HttpUtil httpUtil;
    @Mock
    SignatureUtil signatureUtil;
    @Mock
    KeyVault keyVault;
    @InjectMocks
    Coinbase coinbase;

    @Test
    public void testGetCandlesticks() {
        final var filename = "coinbase/BTC_USDT_20230101T000000Z_20230101T010000Z.json";
        final var jsonResponse = TestUtil.readJsonResource(jsonUtil, filename, CoinbaseProductCandleResponse.class);
        when(httpUtil.get(any(), any(), any(), any())).thenReturn(jsonResponse);
        when(keyVault.get(anyString())).thenReturn("ABC123");
        final var currencyPair = new CurrencyPair("BTC", "USDT");
        final var startInclusive = Instant.parse("2023-01-01T00:00:00Z");
        final var endExclusive = Instant.parse("2023-01-01T01:00:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var candlesticks = coinbase.getCandlesticks(currencyPair, startInclusive, endExclusive, temporalUnit);
        final var candlestickArray = candlesticks.toArray(Candlestick[]::new);

        // First candlestick
        assertEquals(Instant.ofEpochSecond(1672531200), candlestickArray[0].start());
        assertEquals(BigDecimal.valueOf(16541.16), candlestickArray[0].open());
        assertEquals(BigDecimal.valueOf(16541.50), candlestickArray[0].high());
        assertEquals(BigDecimal.valueOf(16541.16), candlestickArray[0].low());
        assertEquals(BigDecimal.valueOf(16541.50), candlestickArray[0].close());
        assertEquals(BigDecimal.valueOf(0.21), candlestickArray[0].volume());

        // Second candlestick
        assertEquals(Instant.ofEpochSecond(1672531320), candlestickArray[1].start());
        assertEquals(BigDecimal.valueOf(16541.02), candlestickArray[1].open());
        assertEquals(BigDecimal.valueOf(16541.02), candlestickArray[1].high());
        assertEquals(BigDecimal.valueOf(16534.49), candlestickArray[1].low());
        assertEquals(BigDecimal.valueOf(16534.58), candlestickArray[1].close());
        assertEquals(BigDecimal.valueOf(0.40198753), candlestickArray[1].volume());

        // Note: Coinbase skips missing candlesticks in the response - e.g. it went from 1672531200 to 1672531320 - so
        // we can't simply loop over the candlesticks and increment the time by the granularity. If Coinbase did not
        // have missing data, the candlesticks.size() would have been 60.
        assertEquals(43, candlesticks.size());
    }

    @Test
    public void testThrowIfRequestExceedsMaxProductCandles() {
        final var currencyPair = new CurrencyPair("BTC", "USDT");
        final var startInclusive = Instant.parse("2023-01-01T00:00:00Z");
        final var endExclusive = Instant.parse("2023-01-01T05:01:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var e = assertThrows(CoinbaseException.class,
                () -> coinbase.getCandlesticks(currencyPair, startInclusive, endExclusive, temporalUnit));
        assertEquals("Too many product candles requested.", e.getMessage());
    }
}
