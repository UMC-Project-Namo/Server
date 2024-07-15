package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.group.type.GroupStatus;

public class GroupStatusConverter extends AbstractEnumAttributeConverter<GroupStatus> {
	private static final String ENUM_NAME = "그룹 상태";

	public GroupStatusConverter() {
		super(GroupStatus.class, false, ENUM_NAME);
	}
}
