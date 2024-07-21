package com.namo.spring.application.external.api.group.service;

import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupUserSearchService {
    private final GroupUserService groupUserService;

    public List<GroupUser> readGroupUsersByGroups(List<Group> groups) {
        return groupUserService.readGroupUsersByGroups(groups);
    }

}
