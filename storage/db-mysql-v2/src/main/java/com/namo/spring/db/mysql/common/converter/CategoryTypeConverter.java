package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.category.type.CategoryType;

public class CategoryTypeConverter extends AbstractEnumAttributeConverter<CategoryType> {
	private static final String ENUM_NAME = "카테고리 타입";

	public CategoryTypeConverter() {
		super(CategoryType.class, false, ENUM_NAME);
	}
}
