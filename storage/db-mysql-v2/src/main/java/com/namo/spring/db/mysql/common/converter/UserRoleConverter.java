package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.user.type.UserRole;

public class UserRoleConverter extends AbstractEnumAttributeConverter<UserRole>{
	private static final String ENUM_NAME = "유저 권한";

	public UserRoleConverter() {
		super(UserRole.class, false, ENUM_NAME);
	}
}
