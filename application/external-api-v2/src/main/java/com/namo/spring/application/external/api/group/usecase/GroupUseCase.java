package com.namo.spring.application.external.api.group.usecase;

import com.namo.spring.application.external.api.group.dto.GroupResponse;
import com.namo.spring.application.external.api.group.service.*;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.core.common.exception.UserException;
import com.namo.spring.db.mysql.domains.group.entity.Group;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;
import com.namo.spring.db.mysql.domains.group.exception.GroupUserException;
import com.namo.spring.db.mysql.domains.group.service.GroupService;
import com.namo.spring.db.mysql.domains.group.service.GroupUserService;
import com.namo.spring.db.mysql.domains.user.entity.User;
import com.namo.spring.db.mysql.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.api.group.converter.GroupResponseConverter.toGroupDtos;
import static com.namo.spring.application.external.api.group.converter.GroupResponseConverter.toGroupParticipantDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class GroupUseCase {
    private final GroupUserSearchService groupUserSearchService;
    private final GroupSearchService groupSearchService;
    private final GroupUpdateService groupUpdateService;
    private final GroupUserSaveService groupUserSaveService;
    private final GroupUserDeleteService groupUserDeleteService;
    private final UserService userService;
    private final GroupService groupService;
    private final GroupSaveService groupSaveService;
    private final GroupUserService groupUserService;

    public User getUser(Long userId) {
        return userService.readUser(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.NOT_FOUND_USER_FAILURE));
    }

    private Group getGroup(Long groupId) {
        return groupService.readGroup(groupId).orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_GROUP_FAILURE));
    }

    private GroupUser getGroupUser(User user, Group group) {
        return groupUserService.readGroupUserByGroupAndUser(group, user).orElseThrow(() -> new GroupUserException(ErrorStatus.NOT_FOUND_GROUP_USER_FAILURE));
    }

    public Long createGroup(Long userId, String groupName, MultipartFile image) {
        return groupSaveService.createGroup(getUser(userId), groupName, image);
    }


    public GroupResponse.GroupJoinDto joinGroup(Long userId, String code) {
        return toGroupParticipantDto(groupUserSaveService.createGroupUser(getUser(userId), code));
    }

    public List<GroupResponse.GroupDto> getGroups(Long userId) {
        List<GroupUser> groupUsersWithGroups = groupSearchService.getGroupUsersWithGroupByUser(getUser(userId));
        List<Group> groups = groupUsersWithGroups
                .stream().map(GroupUser::getGroup)
                .collect(Collectors.toList());
        List<GroupUser> groupUsers = groupUserSearchService.readGroupUsersByGroups(groups);
        return toGroupDtos(groupUsers, groupUsersWithGroups);
    }

    public void modifyCustomGroupName(Long userId, Long groupId, String customGroupName) {
        groupUpdateService.modifyCustomGroupName(getGroupUser(getUser(userId), getGroup(groupId)), customGroupName);
    }

    public void withdrawGroup(Long userId, Long groupId) {
        groupUserDeleteService.removeGroupUser(getGroupUser(getUser(userId), getGroup(groupId)));
    }
}
