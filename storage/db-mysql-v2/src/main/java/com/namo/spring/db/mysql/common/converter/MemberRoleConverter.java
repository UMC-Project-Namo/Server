package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.user.type.MemberRole;

public class MemberRoleConverter extends AbstractEnumAttributeConverter<MemberRole> {
    private static final String ENUM_NAME = "유저 권한";

    public MemberRoleConverter() {
        super(MemberRole.class, false, ENUM_NAME);
    }
}
