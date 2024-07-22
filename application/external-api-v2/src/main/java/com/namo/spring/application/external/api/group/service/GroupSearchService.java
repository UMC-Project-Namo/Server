package com.namo.spring.application.external.api.group.service;

import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupSearchService {
    private final GroupUserService groupUserService;

    public List<GroupUser> getGroupUsersWithGroupByUser(User user) {
        return groupUserService.readGroupUsersWithGroupByUser(user);
    }

}

