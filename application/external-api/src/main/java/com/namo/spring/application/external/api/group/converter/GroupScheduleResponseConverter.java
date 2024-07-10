package com.namo.spring.application.external.api.group.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.group.dto.GroupScheduleResponse;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleResponse;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.user.domain.User;

public class GroupScheduleResponseConverter {
	private GroupScheduleResponseConverter() {
		throw new IllegalStateException("Util Classes");
	}

	public static List<MeetingScheduleResponse.MeetingScheduleDto> toMeetingScheduleDtos(
		List<Schedule> individualsSchedules,
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		List<MoimAndUser> moimAndUsers
	) {
		List<MeetingScheduleResponse.MeetingScheduleDto> result = getMeetingScheduleDtos(individualsSchedules,
			moimAndUsers);

		Map<User, MeetingScheduleResponse.MeetingScheduleUserDto> moimScheduleUserDtoMap = getMeetingScheduleUserDtoMap(
			moimAndUsers);
		Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> moimScheduleMappingUserDtoMap
			= getMeetingScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
		addMeetingSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
		return result;
	}

	public static List<GroupScheduleResponse.GroupScheduleDto> toGroupScheduleDtos(
		List<Schedule> individualsSchedules,
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		List<MoimAndUser> moimAndUsers
	) {
		List<GroupScheduleResponse.GroupScheduleDto> result = getGroupScheduleDtos(individualsSchedules, moimAndUsers);

		Map<User, GroupScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = getGroupScheduleUserDtoMap(
			moimAndUsers);
		Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap
			= getGroupScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
		addGroupSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
		return result;
	}

	private static List<MeetingScheduleResponse.MeetingScheduleDto> getMeetingScheduleDtos(
		List<Schedule> individualsSchedules,
		List<MoimAndUser> moimAndUsers
	) {
		Map<User, Integer> usersColor = moimAndUsers.stream().collect(
			Collectors.toMap(
				MoimAndUser::getUser, MoimAndUser::getColor
			));
		return individualsSchedules.stream()
			.map((schedule -> toMeetingScheduleDto(schedule, usersColor.get(schedule.getUser()))))
			.collect(Collectors.toList());
	}

	private static List<GroupScheduleResponse.GroupScheduleDto> getGroupScheduleDtos(
		List<Schedule> individualsSchedules,
		List<MoimAndUser> moimAndUsers
	) {
		Map<User, Integer> usersColor = moimAndUsers.stream().collect(
			Collectors.toMap(
				MoimAndUser::getUser, MoimAndUser::getColor
			));
		return individualsSchedules.stream()
			.map((schedule -> toGroupScheduleDto(schedule, usersColor.get(schedule.getUser()))))
			.collect(Collectors.toList());
	}

	private static Map<User, MeetingScheduleResponse.MeetingScheduleUserDto> getMeetingScheduleUserDtoMap(
		List<MoimAndUser> moimAndUsers
	) {
		return moimAndUsers.stream()
			.collect(Collectors.toMap(
				MoimAndUser::getUser,
				(GroupScheduleResponseConverter::toMeetingScheduleUserDto)
			));
	}

	private static Map<User, GroupScheduleResponse.MoimScheduleUserDto> getGroupScheduleUserDtoMap(
		List<MoimAndUser> moimAndUsers
	) {
		return moimAndUsers.stream()
			.collect(Collectors.toMap(
				MoimAndUser::getUser,
				(GroupScheduleResponseConverter::toGroupScheduleUserDto)
			));
	}

	private static MeetingScheduleResponse.MeetingScheduleUserDto toMeetingScheduleUserDto(MoimAndUser moimAndUser) {
		return MeetingScheduleResponse.MeetingScheduleUserDto
			.builder()
			.userId(moimAndUser.getUser().getId())
			.userName(moimAndUser.getUser().getName())
			.color(moimAndUser.getColor())
			.build();
	}

	private static GroupScheduleResponse.MoimScheduleUserDto toGroupScheduleUserDto(MoimAndUser moimAndUser) {
		return GroupScheduleResponse.MoimScheduleUserDto
			.builder()
			.userId(moimAndUser.getUser().getId())
			.userName(moimAndUser.getUser().getName())
			.color(moimAndUser.getColor())
			.build();
	}

	private static Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> getMeetingScheduleMappingUserDtoMap(
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		Map<User, MeetingScheduleResponse.MeetingScheduleUserDto> moimScheduleUserDtoMap) {
		return moimScheduleAndUsers.stream().collect(
			Collectors.groupingBy(
				(MoimScheduleAndUser::getMoimSchedule),
				Collectors.mapping(
					(moimScheduleAndUser -> moimScheduleUserDtoMap.get(moimScheduleAndUser.getUser())),
					Collectors.toList()
				)
			)
		);
	}

	private static Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> getGroupScheduleMappingUserDtoMap(
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		Map<User, GroupScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap) {
		return moimScheduleAndUsers.stream().collect(
			Collectors.groupingBy(
				(MoimScheduleAndUser::getMoimSchedule),
				Collectors.mapping(
					(moimScheduleAndUser -> moimScheduleUserDtoMap.get(moimScheduleAndUser.getUser())),
					Collectors.toList()
				)
			)
		);
	}

	private static void addMeetingSchedulesToResult(List<MoimAndUser> moimAndUsers,
		List<MeetingScheduleResponse.MeetingScheduleDto> result,
		Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> moimScheduleMappingUserDtoMap) {
		for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
			boolean isCurGroupSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
			MeetingScheduleResponse.MeetingScheduleDto groupScheduleDto =
				MeetingScheduleResponse.MeetingScheduleDto.fromMeetingSchedule(moimSchedule,
					moimScheduleMappingUserDtoMap.get(moimSchedule),
					isCurGroupSchedule);
			result.add(groupScheduleDto);
		}
	}

	private static void addGroupSchedulesToResult(List<MoimAndUser> moimAndUsers,
		List<GroupScheduleResponse.GroupScheduleDto> result,
		Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap) {
		for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
			boolean isCurGroupSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
			GroupScheduleResponse.GroupScheduleDto groupScheduleDto =
				GroupScheduleResponse.GroupScheduleDto.fromMoimSchedule(moimSchedule,
					moimScheduleMappingUserDtoMap.get(moimSchedule),
					isCurGroupSchedule);
			result.add(groupScheduleDto);
		}
	}

	public static MeetingScheduleResponse.MeetingScheduleDto toMeetingScheduleDto(Schedule schedule, Integer color) {
		List<MeetingScheduleResponse.MeetingScheduleUserDto> users = List.of(
			toMeetingScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
		return MeetingScheduleResponse.MeetingScheduleDto.fromSchedule(schedule, users);
	}

	public static GroupScheduleResponse.GroupScheduleDto toGroupScheduleDto(Schedule schedule, Integer color) {
		List<GroupScheduleResponse.MoimScheduleUserDto> users = List.of(
			toGroupScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
		return GroupScheduleResponse.GroupScheduleDto.fromSchedule(schedule, users);
	}

	public static MeetingScheduleResponse.MeetingScheduleUserDto toMeetingScheduleUserDto(Long userId, String userName,
		Integer color) {
		return MeetingScheduleResponse.MeetingScheduleUserDto.builder()
			.userId(userId)
			.userName(userName)
			.color(color)
			.build();
	}

	public static GroupScheduleResponse.MoimScheduleUserDto toGroupScheduleUserDto(Long userId, String userName,
		Integer color) {
		return GroupScheduleResponse.MoimScheduleUserDto.builder()
			.userId(userId)
			.userName(userName)
			.color(color)
			.build();
	}

}
