package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.user.type.MemberStatus;

public class UserStatusConverter extends AbstractEnumAttributeConverter<MemberStatus> {
	private static final String ENUM_NAME = "유저 상태";

	public UserStatusConverter() {
		super(MemberStatus.class, false, ENUM_NAME);
	}
}
