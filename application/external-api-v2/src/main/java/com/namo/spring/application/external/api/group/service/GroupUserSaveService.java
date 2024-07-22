package com.namo.spring.application.external.api.group.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.category.type.ColorGroup;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.exception.GroupUserException;
import com.namo.spring.db.mysql.domains.group.service.GroupService;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.api.group.converter.GroupUserConverter.toGroupUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupUserSaveService {
    private final GroupUserService groupUserService;
    private final GroupService groupService;

    @Transactional
    public Long createGroupUser(User user, String code) {
        Group group = groupService.readGroupByCode(code).orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_GROUP_FAILURE));
        if (groupUserService.existsGroupUserByGroupAndUser(group, user)) {
            throw new GroupUserException(ErrorStatus.DUPLICATE_PARTICIPATE_FAILURE);
        }
        if (groupUserService.readGroupUsersByGroup(group).size() >= 10) {
            throw new GroupException(ErrorStatus.GROUP_IS_FULL_ERROR);
        }
        GroupUser groupUser = toGroupUser(user, group, selectColor(group));
        groupUserService.createGroupUser(groupUser);
        return group.getId();
    }

    private int selectColor(Group group) {
        Set<String> colors = group.getGroupUsers()
                .stream()
                .map(groupUser -> String.valueOf(groupUser.getColor()))
                .collect(Collectors.toSet());
        return Arrays.stream(ColorGroup.getPaletteColors())
                .map(ColorChip::getCode)
                .filter(code -> !colors.contains(code))
                .findFirst()
                .orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_COLOR));
    }
}
