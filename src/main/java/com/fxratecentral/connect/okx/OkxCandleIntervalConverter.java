package com.fxratecentral.connect.okx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public final class OkxCandleIntervalConverter {
    private OkxCandleIntervalConverter() {
        // Non-instantiable class
    }

    public static String convert(final TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.SECONDS) {
            return "1s";
        }
        if (temporalUnit == ChronoUnit.MINUTES) {
            return "1m";
        }
        // TODO 3m
        // TODO 5m
        // TODO 15m
        // TODO 30m
        if (temporalUnit == ChronoUnit.HOURS) {
            return "1H";
        }
        // TODO 2h
        // TODO 2h
        // TODO 4h
        // TODO 6h
        // TODO 8h
        // TODO 12h
        if (temporalUnit == ChronoUnit.DAYS) {
            return "1D";
        }
        // TODO 3d
        if (temporalUnit == ChronoUnit.WEEKS) {
            return "1W";
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return "1M";
        }
        throw new OkxException("Interval is not supported: " + temporalUnit);
    }
}
