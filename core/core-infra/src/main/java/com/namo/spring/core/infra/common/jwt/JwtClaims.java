package com.namo.spring.core.infra.common.jwt;

import java.util.Map;

public interface JwtClaims {
    Map<String, Object> getClaims();
}
