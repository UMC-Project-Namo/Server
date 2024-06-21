package com.namo.spring.db.mysql.domains.group.repository.group;

import com.namo.spring.db.mysql.domains.group.domain.Moim;

public interface MoimRepositoryCustom {
	Moim findMoimHavingLockWithMoimAndUsersByCode(String code);

	Moim findHavingLockById(Long moimId);
}
