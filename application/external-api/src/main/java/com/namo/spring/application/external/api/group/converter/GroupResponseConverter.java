package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.application.external.api.group.dto.GroupResponse;
import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupResponseConverter {
    private GroupResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static GroupResponse.GroupIdDto toGroupIdDto(Moim group) {
        return GroupResponse.GroupIdDto.builder()
                .groupId(group.getId())
                .build();
    }

    public static List<GroupResponse.GetGroupDto> toGetGroupDtos(List<MoimAndUser> groupAndUsers,
                                                                 List<MoimAndUser> curUserGroupsInUser) {
        Map<Moim, List<MoimAndUser>> groupMappingGroupAndUsers = groupAndUsers.stream()
                .collect(
                        Collectors.groupingBy(
                                MoimAndUser::getMoim
                        )
                );
        return curUserGroupsInUser.stream()
                .map((groupAndUser) -> toGetGroupDto(groupAndUser, groupMappingGroupAndUsers.get(groupAndUser.getMoim())))
                .collect(Collectors.toList());
    }

    public static GroupResponse.GetGroupDto toGetGroupDto(MoimAndUser groupAndUser, List<MoimAndUser> groupAndUsers) {
        return GroupResponse.GetGroupDto.builder()
                .groupId(groupAndUser.getMoim().getId())
                .groupName(groupAndUser.getMoimCustomName())
                .groupImgUrl(groupAndUser.getMoim().getImgUrl())
                .groupCode(groupAndUser.getMoim().getCode())
                .groupUsers(toGetGroupUserDtos(groupAndUsers))
                .build();
    }

    private static List<GroupResponse.GetGroupUserDto> toGetGroupUserDtos(List<MoimAndUser> groupAndUsers) {
        return groupAndUsers.stream()
                .map(GroupResponseConverter::toGetGroupUserDto)
                .collect(Collectors.toList());
    }

    private static GroupResponse.GetGroupUserDto toGetGroupUserDto(MoimAndUser groupAndUser) {
        return GroupResponse.GetGroupUserDto
                .builder()
                .userId(groupAndUser.getUser().getId())
                .userName(groupAndUser.getUser().getName())
                .color(groupAndUser.getColor())
                .build();
    }

    public static GroupResponse.GroupParticipantDto toGetGroupParticipantDto(Moim group) {
        return GroupResponse.GroupParticipantDto.builder()
                .groupId(group.getId())
                .code(group.getCode())
                .build();
    }
}
