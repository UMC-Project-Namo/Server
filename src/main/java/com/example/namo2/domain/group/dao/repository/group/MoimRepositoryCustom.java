package com.example.namo2.domain.group.dao.repository.group;

import com.example.namo2.domain.group.domain.Moim;

public interface MoimRepositoryCustom {
	Moim findMoimHavingLockWithMoimAndUsersByCode(String code);

	Moim findHavingLockById(Long moimId);
}
