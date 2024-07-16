package com.namo.spring.application.external.api.group.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.exception.GroupUserException;
import com.namo.spring.db.mysql.domains.group.service.GroupService;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupUpdateService {
    private final GroupService groupService;
    private final GroupUserService groupUserService;

    @Transactional
    public Long modifyCustomGroupName(Long userId, Long groupId, String customGroupName) {
        //TODO: 추후 User 객체 받아오는 코드 추가 필요
        Group group = groupService.readGroup(groupId).orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_GROUP_FAILURE));
        GroupUser groupUser = groupUserService.readGroupUserByGroupAndUser(group, user).orElseThrow(() -> new GroupUserException(ErrorStatus.NOT_FOUND_GROUP_USER_FAILURE));
        groupUser.updateCustomGroupName(customGroupName);
        return groupId;
    }
}
