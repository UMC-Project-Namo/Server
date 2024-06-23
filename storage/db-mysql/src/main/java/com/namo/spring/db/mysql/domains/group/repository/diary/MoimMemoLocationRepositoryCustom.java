package com.namo.spring.db.mysql.domains.group.repository.diary;

import java.util.List;

import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocation;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;

public interface MoimMemoLocationRepositoryCustom {
	List<MoimMemoLocation> findMoimMemoLocationsWithImgs(MoimSchedule moimSchedule);

	List<MoimMemoLocationAndUser> findMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations);
}
