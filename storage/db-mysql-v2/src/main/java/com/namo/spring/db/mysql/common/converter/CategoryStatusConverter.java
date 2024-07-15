package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.category.type.CategoryStatus;

public class CategoryStatusConverter extends AbstractEnumAttributeConverter<CategoryStatus> {
	private static final String ENUM_NAME = "카테고리 상태";

	public CategoryStatusConverter() {
		super(CategoryStatus.class, false, ENUM_NAME);
	}
}
