package com.fxratecentral.connect.binance;

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
public class BinanceTest {
    private final JsonUtil jsonUtil = new JsonUtil();
    @Mock
    private HttpUtil httpUtil;
    @Mock
    private SignatureUtil signatureUtil;
    @Mock
    private KeyVault keyVault;
    @InjectMocks
    private Binance binance;

    @Test
    public void testGetCandlesticks() {
        final var filename = "binance/BTC_USDT_20180101T000000Z_20180101T160000Z.json";
        final var jsonResponse = TestUtil.readJsonResource(jsonUtil, filename, Object[][].class);
        when(httpUtil.get(any(), any(), any(), any())).thenReturn(jsonResponse);
        when(keyVault.get(anyString())).thenReturn("ABC123");
        final var currencyPair = new CurrencyPair("BTC", "USDT");
        final var startInclusive = Instant.parse("2018-01-01T00:00:00Z");
        final var endExclusive = Instant.parse("2018-01-01T16:00:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var candlesticks = binance.getCandlesticks(currencyPair, startInclusive, endExclusive, temporalUnit);
        final var candlestickArray = candlesticks.toArray(Candlestick[]::new);
        assertEquals(Instant.parse("2018-01-01T00:00:00Z"), candlestickArray[0].start());
        assertEquals(Instant.parse("2018-01-01T15:59:00Z"), candlestickArray[candlesticks.size() - 1].start());

        // First candlestick
        assertEquals(Instant.parse("2018-01-01T00:00:00Z"), candlestickArray[0].start());
        assertEquals(new BigDecimal("13715.65000000"), candlestickArray[0].open());
        assertEquals(new BigDecimal("13715.65000000"), candlestickArray[0].high());
        assertEquals(new BigDecimal("13681.00000000"), candlestickArray[0].low());
        assertEquals(new BigDecimal("13707.92000000"), candlestickArray[0].close());
        assertEquals(new BigDecimal("2.84426600"), candlestickArray[0].volume());

        // Second candlestick
        assertEquals(Instant.parse("2018-01-01T00:01:00Z"), candlestickArray[1].start());
        assertEquals(new BigDecimal("13707.91000000"), candlestickArray[1].open());
        assertEquals(new BigDecimal("13707.91000000"), candlestickArray[1].high());
        assertEquals(new BigDecimal("13666.11000000"), candlestickArray[1].low());
        assertEquals(new BigDecimal("13694.92000000"), candlestickArray[1].close());
        assertEquals(new BigDecimal("2.11313800"), candlestickArray[1].volume());
    }

    @Test
    public void testThrowIfRequestExceedsMaxProductCandles() {
        final var currencyPair = new CurrencyPair("BTC", "USDT");
        final var startInclusive = Instant.parse("2010-12-31T00:00:00Z");
        final var endExclusive = Instant.parse("2010-12-31T16:42:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var e = assertThrows(BinanceException.class,
            () -> binance.getCandlesticks(currencyPair, startInclusive, endExclusive, temporalUnit));
        assertEquals("Too many klines requested.", e.getMessage());
    }
}
