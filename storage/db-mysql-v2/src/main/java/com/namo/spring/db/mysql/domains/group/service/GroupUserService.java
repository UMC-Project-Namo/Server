package com.namo.spring.db.mysql.domains.group.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.repository.GroupUserRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class GroupUserService {
	private final GroupUserRepository groupUserRepository;

	@Transactional
	public GroupUser createGroupUser(GroupUser groupUser) {
		return groupUserRepository.save(groupUser);
	}

	@Transactional(readOnly = true)
	public Optional<GroupUser> readGroupUser(Long groupUserId) {
		return groupUserRepository.findById(groupUserId);
	}

	@Transactional
	public void deleteGroupUser(Long groupUserId) {
		groupUserRepository.deleteById(groupUserId);
	}
}
