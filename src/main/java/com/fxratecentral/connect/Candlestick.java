package com.fxratecentral.connect;

import java.math.BigDecimal;
import java.time.Instant;

public record Candlestick(
    Instant start,
    BigDecimal open,
    BigDecimal high,
    BigDecimal low,
    BigDecimal close,
    BigDecimal volume
) {
    // Empty.
}
