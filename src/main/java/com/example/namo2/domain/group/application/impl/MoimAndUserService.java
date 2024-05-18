package com.example.namo2.domain.group.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.group.dao.repository.group.MoimAndUserRepository;
import com.example.namo2.domain.group.domain.Moim;
import com.example.namo2.domain.group.domain.MoimAndUser;

import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimAndUserService {
	private final MoimAndUserRepository moimAndUserRepository;

	public MoimAndUser create(MoimAndUser moimAndUser, Moim moim) {
		validateExistsMoimAndUser(moim, moimAndUser);
		validateMoimIsFull(moim);
		MoimAndUser savedMoimAndUser = moimAndUserRepository.save(moimAndUser);
		moim.addMember(savedMoimAndUser);
		return savedMoimAndUser;
	}

	private void validateExistsMoimAndUser(Moim moim, MoimAndUser moimAndUser) {
		if (moim.containUser(moimAndUser.getUser())) {
			throw new BaseException(BaseResponseStatus.DUPLICATE_PARTICIPATE_FAILURE);
		}
	}

	private void validateMoimIsFull(Moim moim) {
		if (moim.isFull()) {
			throw new BaseException(BaseResponseStatus.MOIM_IS_FULL_ERROR);
		}
	}

	public List<MoimAndUser> getMoimAndUsers(User user) {
		return moimAndUserRepository.findMoimAndUserByUser(user);
	}

	public List<MoimAndUser> getMoimAndUsers(Moim moim) {
		return moimAndUserRepository.findMoimAndUserByMoim(moim);
	}

	public List<MoimAndUser> getMoimAndUsers(List<Moim> moims) {
		return moimAndUserRepository.findMoimAndUserByMoim(moims);
	}

	public MoimAndUser getMoimAndUser(Moim moim, User user) {
		return moimAndUserRepository.findMoimAndUserByUserAndMoim(user, moim)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
	}

	public void removeMoimAndUser(MoimAndUser moimAndUser, Moim moim) {
		validateNotExistsMoimAndUser(moim, moimAndUser);
		if (moim.isLastMember()) {
			moim.removeMoim();
		}
		moim.removeMember();
		moimAndUserRepository.delete(moimAndUser);
	}

	private void validateNotExistsMoimAndUser(Moim moim, MoimAndUser moimAndUser) {
		if (!moim.containUser(moimAndUser.getUser())) {
			throw new BaseException(BaseResponseStatus.NOT_INCLUDE_MOIM_USER);
		}
	}

	public void removeMoimAndUsersByUser(User user) {
		moimAndUserRepository.deleteAllByUser(user);
	}
}
