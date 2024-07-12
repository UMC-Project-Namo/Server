package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.user.type.UserStatus;

public class UserStatusConverter extends AbstractEnumAttributeConverter<UserStatus> {
	private static final String ENUM_NAME = "유저 권한";

	public UserStatusConverter() {
		super(UserStatus.class, false, ENUM_NAME);
	}
}
