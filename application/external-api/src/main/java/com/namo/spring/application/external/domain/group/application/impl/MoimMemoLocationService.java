package com.namo.spring.application.external.domain.group.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.domain.group.domain.MoimMemo;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocation;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocationImg;
import com.namo.spring.application.external.domain.group.domain.MoimSchedule;
import com.namo.spring.application.external.domain.group.repository.diary.MoimMemoLocationAndUserRepository;
import com.namo.spring.application.external.domain.group.repository.diary.MoimMemoLocationImgRepository;
import com.namo.spring.application.external.domain.group.repository.diary.MoimMemoLocationRepository;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MoimMemoLocationService {
	private final MoimMemoLocationRepository moimMemoLocationRepository;
	private final MoimMemoLocationAndUserRepository moimMemoLocationAndUserRepository;
	private final MoimMemoLocationImgRepository moimMemoLocationImgRepository;

	public MoimMemoLocation createMoimMemoLocation(MoimMemoLocation moimMemoLocation, MoimMemo moimMemo) {
		if (moimMemo.isFullLocationSize()) {
			throw new GroupException(ErrorStatus.MOIM_MEMO_IS_FULL_ERROR);
		}
		return moimMemoLocationRepository.save(moimMemoLocation);
	}

	public List<MoimMemoLocationAndUser> createMoimMemoLocationAndUsers(
		List<MoimMemoLocationAndUser> moimMemoLocations) {
		return moimMemoLocationAndUserRepository.saveAll(moimMemoLocations);
	}

	public MoimMemoLocationImg createMoimMemoLocationImg(MoimMemoLocationImg moimMemoLocationImg) {
		return moimMemoLocationImgRepository.save(moimMemoLocationImg);
	}

	public MoimMemoLocation getMoimMemoLocationWithImgs(Long memoLocationId) {
		return moimMemoLocationRepository.findMoimMemoLocationWithImgsById(memoLocationId)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_MOIM_MEMO_LOCATION_FAILURE));
	}

	public void removeMoimMemoLocationAndUsers(MoimMemoLocation moimMemoLocation) {
		moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
	}

	public void removeMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocation) {
		moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
	}

	public void removeMoimMemoLocationAndUsersByUser(User user) {
		moimMemoLocationAndUserRepository.deleteAllByUser(user);
	}

	public void removeMoimMemoLocationImgs(MoimMemoLocation moimMemoLocation) {
		moimMemoLocationImgRepository.deleteMoimMemoLocationImgByMoimMemoLocation(moimMemoLocation);
	}

	public void removeMoimMemoLocationImgs(List<MoimMemoLocation> moimMemoLocations) {
		moimMemoLocationImgRepository.deleteMoimMemoLocationImgByMoimMemoLocation(moimMemoLocations);
	}

	public void removeMoimMemoLocation(MoimMemoLocation moimMemoLocation) {
		moimMemoLocationRepository.delete(moimMemoLocation);
	}

	public List<MoimMemoLocation> getMoimMemoLocationWithImgs(MoimMemo moimMemo) {
		return moimMemoLocationRepository.findMoimMemo(moimMemo);
	}

	public List<MoimMemoLocationImg> getMoimMemoLocationImgs(List<MoimMemoLocation> moimMemoLocations) {
		return moimMemoLocationImgRepository
			.findMoimMemoLocationImgsByMoimMemoLocations(moimMemoLocations);
	}

	public List<MoimMemoLocation> getMoimMemoLocations(MoimSchedule moimSchedule) {
		return moimMemoLocationRepository.findMoimMemoLocationsWithImgs(moimSchedule);
	}

	public List<MoimMemoLocationAndUser> getMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations) {
		return moimMemoLocationRepository.findMoimMemoLocationAndUsers(moimMemoLocations);
	}

}
