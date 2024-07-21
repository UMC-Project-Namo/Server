package com.namo.spring.application.external.api.group.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.exception.GroupUserException;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import com.namo.spring.db.mysql.domains.user.entity.User;
import com.namo.spring.db.mysql.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupSearchService {
    private final GroupUserService groupUserService;
    private final UserService userService;

    public List<GroupUser> getGroupUsersWithGroupByUser(User user) {
        return groupUserService.readGroupUsersWithGroupByUser(user);
    }

    public void readGroupUserByGroupAndUser(Group group, User user) {
        GroupUser groupUser = groupUserService.readGroupUserByGroupAndUser(group, user).orElseThrow(() -> new GroupUserException(ErrorStatus.NOT_FOUND_GROUP_USER_FAILURE));
    }

}

