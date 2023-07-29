package com.fxratecentral.connect.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignatureUtilTest {
    private SignatureUtil signatureUtil;

    @BeforeEach
    public void setup() {
        signatureUtil = new SignatureUtil();
    }

    @Test
    public void testSign() {
        // Example from https://developers.binance.com/docs/binance-trading-api/spot
        final var secretKey = "NhqPtmdSJYdKjVHjA7PZj4Mge3R5YNiP1e3UZjInClVN65XAbvqqM6A7H5fATj0j";
        var data = "symbol=LTCBTC";
        data += "&side=BUY";
        data += "&type=LIMIT";
        data += "&timeInForce=GTC";
        data += "&quantity=1";
        data += "&price=0.1";
        data += "&recvWindow=5000";
        data += "&timestamp=1499827319559";
        final var expected = "c8db56825ae71d6d79447849e617115f4a920fa2acdcab2b053c4b2838bd6b71";
        final var actual = signatureUtil.sign(secretKey, data);
        assertEquals(expected, actual);
    }
}
