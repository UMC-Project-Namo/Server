package com.namo.spring.application.external.api.group.service;

import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.repository.diary.MoimMemoRepository;
import com.namo.spring.db.mysql.domains.group.repository.group.MoimRepository;
import com.namo.spring.db.mysql.domains.group.repository.schedule.MoimScheduleAndUserRepository;
import com.namo.spring.db.mysql.domains.group.repository.schedule.MoimScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimService {
	private final MoimRepository moimRepository;
	private final MoimScheduleRepository moimScheduleRepository;
	private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
	private final MoimMemoRepository moimMemoRepository;

	public Moim create(Moim moim) {
		return moimRepository.save(moim);
	}

	public Moim getMoimWithMoimAndUsersByMoimId(Long moimId) {
		return moimRepository.findMoimWithMoimAndUsersByMoimId(moimId)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_MOIM_FAILURE));
	}

	public Moim getMoimHavingLockById(Long moimId) {
		Moim moim = moimRepository.findHavingLockById(moimId);
		if (moim == null) {
			throw new GroupException(ErrorStatus.NOT_FOUND_MOIM_FAILURE);
		}
		return moim;
	}

	public Moim getMoimWithMoimAndUsersByCode(String code) {
		Moim moim = moimRepository.findMoimHavingLockWithMoimAndUsersByCode(code);
		if (moim == null) {
			throw new GroupException(ErrorStatus.NOT_FOUND_MOIM_FAILURE);
		}
		return moim;
	}

	public void removeSchedule(Long moimScheduleId) {
		MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
		MoimMemo moimMemo = moimMemoRepository.findMoimMemoAndLocationsByMoimSchedule(moimSchedule);

		/*
		         모임 메모가 있는 경우 모임 메모 장소를 모두 삭제 후 모임 메모 삭제
		        if (moimMemo != null) {
		            moimMemo.getMoimMemoLocations()
		                    .stream()
		                    .forEach(moimMemoLocation ->
		                    	moimMemoService.removeMoimMemoLocation(moimMemoLocation.getId()));
		            moimMemoRepository.delete(moimMemo);
		        }
		*/

		moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
		moimScheduleRepository.delete(moimSchedule);
	}
}
