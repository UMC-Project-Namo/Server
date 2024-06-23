package com.namo.spring.application.external.api.group.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.group.repository.group.MoimAndUserRepository;
import com.namo.spring.db.mysql.domains.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimAndUserService {
	private final MoimAndUserRepository moimAndUserRepository;

	public MoimAndUser createMoimAndUser(MoimAndUser moimAndUser, Moim moim) {
		validateExistsMoimAndUser(moim, moimAndUser);
		validateMoimIsFull(moim);
		MoimAndUser savedMoimAndUser = moimAndUserRepository.save(moimAndUser);
		moim.addMember(savedMoimAndUser);
		return savedMoimAndUser;
	}

	private void validateExistsMoimAndUser(Moim moim, MoimAndUser moimAndUser) {
		if (moim.containUser(moimAndUser.getUser())) {
			throw new GroupException(ErrorStatus.DUPLICATE_PARTICIPATE_FAILURE);
		}
	}

	private void validateMoimIsFull(Moim moim) {
		if (moim.isFull()) {
			throw new GroupException(ErrorStatus.MOIM_IS_FULL_ERROR);
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
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_MOIM_AND_USER_FAILURE));
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
			throw new GroupException(ErrorStatus.NOT_INCLUDE_MOIM_USER);
		}
	}

	public void removeMoimAndUsersByUser(User user) {
		moimAndUserRepository.deleteAllByUser(user);
	}
}
