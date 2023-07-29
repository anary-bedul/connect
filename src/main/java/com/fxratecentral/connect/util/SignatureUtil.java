package com.fxratecentral.connect.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class SignatureUtil {
    public static final String ALGORITHM = "HmacSHA256";

    public String sign(final String secretKey, final String message) {
        try {
            final var hmac = Mac.getInstance(ALGORITHM);
            final var keySpec = new SecretKeySpec(toBytes(secretKey), ALGORITHM);
            hmac.init(keySpec);
            return HexFormat.of().formatHex(hmac.doFinal(toBytes(message)));
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to sign message.", e);
        }
    }

    private static byte[] toBytes(final String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }
}
