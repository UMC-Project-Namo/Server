package com.namo.spring.db.mysql.domains.group.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<Group> readGroupWithGroupUsers(Long groupId) {
        return groupRepository.findGroupWithGroupUsers(groupId);
    }

    @Transactional(readOnly = true)
    public Optional<Group> readGroupByCode(String code) {
        return groupRepository.findGroupWithGroupUsersByCode(code);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}
