package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.application.external.api.group.dto.GroupResponse;
import com.namo.spring.db.mysql.domains.group.entity.GroupUser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupResponseConverter {
    public static List<GroupResponse.GroupDto> toGroupDtos(List<GroupUser> groupUsers, List<GroupUser> groups) {
        Map<Long, List<GroupUser>> groupMappingGroupUsers = groupUsers.stream()
                .collect(
                        Collectors.groupingBy(groupUser -> groupUser.getGroup().getId())
                );
        return groups.stream()
                .map((group) -> toGroupDto(group, groupMappingGroupUsers.get(group.getGroup().getId())))
                .collect(Collectors.toList());
    }

    public static GroupResponse.GroupDto toGroupDto(GroupUser groupUser, List<GroupUser> groupUsers) {
        return GroupResponse.GroupDto.builder()
                .groupId(groupUser.getGroup().getId())
                .groupName(groupUser.getCustomGroupName())
                .groupImgUrl(groupUser.getGroup().getProfileImg())
                .groupCode(groupUser.getGroup().getCode())
                .groupUsers(toGroupUserDtos(groupUsers))
                .build();
    }

    private static List<GroupResponse.GroupUserDto> toGroupUserDtos(List<GroupUser> groupUsers) {
        return groupUsers.stream()
                .map(GroupResponseConverter::toGroupUserDto)
                .collect(Collectors.toList());
    }

    private static GroupResponse.GroupUserDto toGroupUserDto(GroupUser groupUser) {
        return GroupResponse.GroupUserDto
                .builder()
                .userId(groupUser.getUser().getId())
                .userName(groupUser.getUser().getUsername())
                .color(groupUser.getColor())
                .build();
    }

    public static GroupResponse.GroupJoinDto toGroupParticipantDto(Long groupId) {
        return GroupResponse.GroupJoinDto
                .builder()
                .groupId(groupId)
                .build();
    }

}
