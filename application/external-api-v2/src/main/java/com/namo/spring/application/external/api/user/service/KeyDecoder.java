package com.namo.spring.application.external.api.user.service;

import java.math.BigInteger;
import java.util.Base64;

public abstract class KeyDecoder {

    private static final int POSITIVE_SIG_NUM = 1;

    public static BigInteger decodeToBigInteger(String base64String) {
        byte[] decoded = Base64.getUrlDecoder().decode(base64String);
        return new BigInteger(POSITIVE_SIG_NUM, decoded);
    }
}
