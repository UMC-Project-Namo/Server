package com.namo.spring.application.external.domain.group.repository.group;

import com.namo.spring.application.external.domain.group.domain.Moim;

public interface MoimRepositoryCustom {
	Moim findMoimHavingLockWithMoimAndUsersByCode(String code);

	Moim findHavingLockById(Long moimId);
}
