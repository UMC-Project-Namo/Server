package com.example.namo2.domain.group.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.group.dao.repository.diary.MoimMemoRepository;
import com.example.namo2.domain.group.domain.MoimMemo;

import com.example.namo2.domain.group.dao.repository.group.MoimRepository;
import com.example.namo2.domain.group.dao.repository.schedule.MoimScheduleAndUserRepository;
import com.example.namo2.domain.group.dao.repository.schedule.MoimScheduleRepository;
import com.example.namo2.domain.group.domain.Moim;
import com.example.namo2.domain.group.domain.MoimSchedule;

import com.example.namo2.global.common.exception.BaseException;

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
			.orElseThrow(() -> new BaseException(NOT_FOUND_MOIM_FAILURE));
	}

	public Moim getMoimHavingLockById(Long moimId) {
		Moim moim = moimRepository.findHavingLockById(moimId);
		if (moim == null) {
			throw new BaseException(NOT_FOUND_MOIM_FAILURE);
		}
		return moim;
	}

	public Moim getMoimWithMoimAndUsersByCode(String code) {
		Moim moim = moimRepository.findMoimHavingLockWithMoimAndUsersByCode(code);
		if (moim == null) {
			throw new BaseException(NOT_FOUND_MOIM_FAILURE);
		}
		return moim;
	}

	public void removeSchedule(Long moimScheduleId) {
		MoimSchedule moimSchedule = moimScheduleRepository.findById(moimScheduleId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
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
