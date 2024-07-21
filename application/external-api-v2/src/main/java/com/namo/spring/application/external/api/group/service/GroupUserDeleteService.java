package com.namo.spring.application.external.api.group.service;

import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupUserDeleteService {
    private final GroupUserService groupUserService;

    @Transactional
    public void removeGroupUser(GroupUser groupUser) {
        groupUserService.deleteGroupUser(groupUser.getId());
    }
}