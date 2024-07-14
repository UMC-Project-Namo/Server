package com.namo.spring.db.mysql.domains.group.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class GroupService {
	private final GroupRepository groupRepository;

	@Transactional
	public Group createGroup(Group group) {
		return groupRepository.save(group);
	}

	@Transactional(readOnly = true)
	public Optional<Group> readGroup(Long groupId) {
		return groupRepository.findById(groupId);
	}

	@Transactional
	public void deleteGroup(Long groupId) {
		groupRepository.deleteById(groupId);
	}
}
