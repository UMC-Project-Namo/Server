package com.namo.spring.application.external.api.group.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.group.dto.GroupResponse;
import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;

public class GroupResponseConverter {
	private GroupResponseConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static GroupResponse.GroupIdDto toMoimIdDto(Moim moim) {
		return GroupResponse.GroupIdDto.builder()
			.groupId(moim.getId())
			.build();
	}

	public static List<GroupResponse.GroupDto> toMoimDtos(List<MoimAndUser> moimAndUsers,
		List<MoimAndUser> curUserMoimsInUser) {
		Map<Moim, List<MoimAndUser>> moimMappingMoimAndUsers = moimAndUsers.stream()
			.collect(
				Collectors.groupingBy(
					MoimAndUser::getMoim
				)
			);
		return curUserMoimsInUser.stream()
			.map((moimAndUser) -> toMoimDto(moimAndUser, moimMappingMoimAndUsers.get(moimAndUser.getMoim())))
			.collect(Collectors.toList());
	}

	public static GroupResponse.GroupDto toMoimDto(MoimAndUser moimAndUser, List<MoimAndUser> moimAndUsers) {
		return GroupResponse.GroupDto.builder()
			.groupId(moimAndUser.getMoim().getId())
			.groupName(moimAndUser.getMoimCustomName())
			.groupImgUrl(moimAndUser.getMoim().getImgUrl())
			.groupCode(moimAndUser.getMoim().getCode())
			.groupUsers(toMoimUserDtos(moimAndUsers))
			.build();
	}

	private static List<GroupResponse.GroupUserDto> toMoimUserDtos(List<MoimAndUser> moimAndUsers) {
		return moimAndUsers.stream()
			.map(GroupResponseConverter::toMoimUserDto)
			.collect(Collectors.toList());
	}

	private static GroupResponse.GroupUserDto toMoimUserDto(MoimAndUser moimAndUser) {
		return GroupResponse.GroupUserDto
			.builder()
			.userId(moimAndUser.getUser().getId())
			.userName(moimAndUser.getUser().getName())
			.color(moimAndUser.getColor())
			.build();
	}

	public static GroupResponse.GroupParticipantDto toMoimParticipantDto(Moim moim) {
		return GroupResponse.GroupParticipantDto.builder()
			.groupId(moim.getId())
			.code(moim.getCode())
			.build();
	}
}
