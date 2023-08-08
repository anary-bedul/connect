package com.fxratecentral.connect.okx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fxratecentral.connect.Candlestick;
import com.fxratecentral.connect.CurrencyPair;
import com.fxratecentral.connect.KeyVault;
import com.fxratecentral.connect.TestUtil;
import com.fxratecentral.connect.util.HttpUtil;
import com.fxratecentral.connect.util.JsonUtil;
import com.fxratecentral.connect.util.SignatureUtil;

@ExtendWith(MockitoExtension.class)
public class OkxTest {
    private final JsonUtil jsonUtil = new JsonUtil();
    @Mock
    private HttpUtil httpUtil;
    @Mock
    private SignatureUtil signatureUtil;
    @Mock
    private KeyVault keyVault;
    @InjectMocks
    private Okx okx;

    @Test
    public void testGetCandlesticks() {
        final var filename = "okx/okx_2023_08_07T11_40_to_2023_08_07T14_53_candles.json";
        final var jsonResponse =
            TestUtil.readJsonResource(jsonUtil, filename, OkxResponseClass.class);
        when(httpUtil.get(any(), any(), any(), any())).thenReturn(jsonResponse);
        final var currencyPair = new CurrencyPair("BTC", "USDT");
        final var startInclusive = Instant.parse("2023-08-07T11:40:00Z");
        final var endExclusive = Instant.parse("2023-08-07T14:53:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var candlesticks =
            okx.getCandlesticks(currencyPair, startInclusive, endExclusive, temporalUnit);
        final var candlestickArray = candlesticks.toArray(Candlestick[]::new);
        assertEquals(Instant.parse("2023-08-07T11:52:00Z"), candlestickArray[0].start());
        assertEquals(Instant.parse("2023-08-07T11:41:00Z"),
            candlestickArray[candlesticks.size() - 1].start());

        // First candlestick
        assertEquals(Instant.parse("2023-08-07T11:52:00Z"), candlestickArray[0].start());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[0].open());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[0].high());
        assertEquals(new BigDecimal("29084.2"), candlestickArray[0].low());
        assertEquals(new BigDecimal("29084.3"), candlestickArray[0].close());
        assertEquals(new BigDecimal("4.55494971"), candlestickArray[0].volume());

        // Second candlestick
        assertEquals(Instant.parse("2023-08-07T11:51:00Z"), candlestickArray[1].start());
        assertEquals(new BigDecimal("29086.7"), candlestickArray[1].open());
        assertEquals(new BigDecimal("29086.8"), candlestickArray[1].high());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[1].low());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[1].close());
        assertEquals(new BigDecimal("5.55576423"), candlestickArray[1].volume());

    }

    @Test
    public void testThrowIfResponseNotSuccessCandles() {
        final var filename = "okx/okx_invalid_instrument_id_not_found_response.json";
        final var jsonResponse =
            TestUtil.readJsonResource(jsonUtil, filename, OkxResponseClass.class);
        when(httpUtil.get(any(), any(), any(), any())).thenReturn(jsonResponse);
        final var currencyPair = new CurrencyPair("BTCX", "USDT");
        final var startInclusive = Instant.parse("2010-12-31T00:00:00Z");
        final var endExclusive = Instant.parse("2010-12-31T16:42:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var e = assertThrows(OkxException.class, () -> okx.getCandlesticks(currencyPair,
            startInclusive, endExclusive, temporalUnit));
        assertEquals("Error Code = 51001, Message = Instrument ID does not exist", e.getMessage());
    }

    @Test
    public void testGetCandlesticksHistory() {
        final var filename = "okx/okx_2023_08_07T11_40_to_2023_08_07T14_53_candles.json";
        final var jsonResponse =
            TestUtil.readJsonResource(jsonUtil, filename, OkxResponseClass.class);
        when(httpUtil.get(any(), any(), any(), any())).thenReturn(jsonResponse);
        final var currencyPair = new CurrencyPair("BTC", "USDT");
        final var startInclusive = Instant.parse("2023-08-07T11:40:00Z");
        final var endExclusive = Instant.parse("2023-08-07T14:53:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var candlesticks = okx.getCandlesticksHistory(currencyPair, startInclusive,
            endExclusive, temporalUnit);
        final var candlestickArray = candlesticks.toArray(Candlestick[]::new);
        assertEquals(Instant.parse("2023-08-07T11:52:00Z"), candlestickArray[0].start());
        assertEquals(Instant.parse("2023-08-07T11:41:00Z"),
            candlestickArray[candlesticks.size() - 1].start());

        // First candlestick
        assertEquals(Instant.parse("2023-08-07T11:52:00Z"), candlestickArray[0].start());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[0].open());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[0].high());
        assertEquals(new BigDecimal("29084.2"), candlestickArray[0].low());
        assertEquals(new BigDecimal("29084.3"), candlestickArray[0].close());
        assertEquals(new BigDecimal("4.55494971"), candlestickArray[0].volume());

        // Second candlestick
        assertEquals(Instant.parse("2023-08-07T11:51:00Z"), candlestickArray[1].start());
        assertEquals(new BigDecimal("29086.7"), candlestickArray[1].open());
        assertEquals(new BigDecimal("29086.8"), candlestickArray[1].high());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[1].low());
        assertEquals(new BigDecimal("29085.1"), candlestickArray[1].close());
        assertEquals(new BigDecimal("5.55576423"), candlestickArray[1].volume());
    }

    @Test
    public void testThrowIfResponseNotSuccessCandlesHistory() {
        final var filename = "okx/okx_invalid_instrument_id_not_found_response.json";
        final var jsonResponse =
            TestUtil.readJsonResource(jsonUtil, filename, OkxResponseClass.class);
        when(httpUtil.get(any(), any(), any(), any())).thenReturn(jsonResponse);
        final var currencyPair = new CurrencyPair("BTCX", "USDT");
        final var startInclusive = Instant.parse("2010-12-31T00:00:00Z");
        final var endExclusive = Instant.parse("2010-12-31T16:42:00Z");
        final var temporalUnit = ChronoUnit.MINUTES;
        final var e = assertThrows(OkxException.class, () -> okx
            .getCandlesticksHistory(currencyPair, startInclusive, endExclusive, temporalUnit));
        assertEquals("Error Code = 51001, Message = Instrument ID does not exist", e.getMessage());
    }
}
