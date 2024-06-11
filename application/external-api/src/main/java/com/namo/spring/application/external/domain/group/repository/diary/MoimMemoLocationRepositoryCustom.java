package com.namo.spring.application.external.domain.group.repository.diary;

import java.util.List;

import com.namo.spring.application.external.domain.group.domain.MoimMemoLocation;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.application.external.domain.group.domain.MoimSchedule;

public interface MoimMemoLocationRepositoryCustom {
	List<MoimMemoLocation> findMoimMemoLocationsWithImgs(MoimSchedule moimSchedule);

	List<MoimMemoLocationAndUser> findMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations);
}
