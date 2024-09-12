package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.oauth.type.Provider;

public class ProviderConverter extends AbstractEnumAttributeConverter<Provider> {
    private static final String ENUM_NAME = "소셜 로그인 제공자";

    public ProviderConverter() {
        super(Provider.class, false, ENUM_NAME);
    }
}
