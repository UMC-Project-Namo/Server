package com.namo.spring.db.mysql.domains.group.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.repository.GroupUserRepository;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<GroupUser> readGroupUserByGroupAndUser(Group group, User user) {
        return groupUserRepository.findGroupUserByGroupAndUser(group, user);
    }

    @Transactional(readOnly = true)
    public List<GroupUser> readGroupUsersWithGroupByUser(User user) {
        return groupUserRepository.findGroupUsersWithGroupByUser(user);
    }

    @Transactional(readOnly = true)
    public List<GroupUser> readGroupUsersByGroups(List<Group> groups) {
        return groupUserRepository.findGroupUsersByGroups(groups);
    }

    @Transactional(readOnly = true)
    public List<GroupUser> readGroupUsersByGroup(Group group) {
        return groupUserRepository.findGroupUsersByGroup(group);
    }

    @Transactional(readOnly = true)
    public boolean existsGroupUserByGroupAndUser(Group group, User user) {
        return groupUserRepository.existsGroupUserByGroupAndUser(group, user);
    }

    @Transactional
    public void deleteGroupUser(Long groupUserId) {
        groupUserRepository.deleteById(groupUserId);
    }

}
