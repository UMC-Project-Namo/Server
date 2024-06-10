package com.example.namo2.domain.group.dao.repository.diary;

import java.util.List;

import com.example.namo2.domain.group.domain.MoimMemoLocation;
import com.example.namo2.domain.group.domain.MoimMemoLocationAndUser;

import com.example.namo2.domain.group.domain.MoimSchedule;

public interface MoimMemoLocationRepositoryCustom {
	List<MoimMemoLocation> findMoimMemoLocationsWithImgs(MoimSchedule moimSchedule);

	List<MoimMemoLocationAndUser> findMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations);
}
