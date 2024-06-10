package com.example.namo2.domain.group.application.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.group.dao.repository.diary.MoimMemoRepository;
import com.example.namo2.domain.group.domain.MoimMemo;

import com.example.namo2.domain.group.domain.MoimSchedule;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimMemoService {
	private final MoimMemoRepository moimMemoRepository;

	public Optional<MoimMemo> getMoimMemoOrNull(MoimSchedule moimSchedule) {
		return moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule);
	}

	public MoimMemo getMoimMemo(MoimSchedule moimSchedule) {
		return moimMemoRepository.findMoimMemoByMoimSchedule(moimSchedule)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_FAILURE));
	}

	public MoimMemo getMoimMemoWithUsers(MoimSchedule moimSchedule) {
		return moimMemoRepository.findMoimMemoAndUsersByMoimSchedule(moimSchedule);
	}

	public void removeMoimMemo(MoimMemo moimMemo) {
		moimMemoRepository.delete(moimMemo);
	}

	public MoimMemo create(MoimMemo moimMemo) {
		return moimMemoRepository.save(moimMemo);
	}

	public MoimMemo getMoimMemoWithLocations(Long moimScheduleId) {
		return moimMemoRepository.findMoimMemoAndLocationsByMoimSchedule(moimScheduleId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_MEMO_FAILURE));
	}
}
