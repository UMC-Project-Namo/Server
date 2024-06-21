package com.namo.spring.application.external.api.group.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.repository.diary.MoimMemoRepository;

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
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_MOIM_FAILURE));
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
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_MOIM_MEMO_FAILURE));
	}
}
