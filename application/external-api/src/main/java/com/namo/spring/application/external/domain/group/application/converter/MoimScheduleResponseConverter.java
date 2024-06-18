package com.namo.spring.application.external.domain.group.application.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.application.external.domain.group.ui.dto.GroupScheduleResponse;
import com.namo.spring.application.external.domain.group.domain.MoimAndUser;
import com.namo.spring.application.external.domain.group.domain.MoimSchedule;
import com.namo.spring.application.external.domain.group.domain.MoimScheduleAndUser;

import com.namo.spring.application.external.domain.individual.domain.Schedule;

import com.namo.spring.db.mysql.domains.user.domain.User;

public class MoimScheduleResponseConverter {
	private MoimScheduleResponseConverter() {
		throw new IllegalStateException("Util Classes");
	}

	public static List<GroupScheduleResponse.MoimScheduleDto> toMoimScheduleDtos(
		List<Schedule> indivisualsSchedules,
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		List<MoimAndUser> moimAndUsers
	) {
		List<GroupScheduleResponse.MoimScheduleDto> result = getMoimScheduleDtos(indivisualsSchedules, moimAndUsers);

		Map<User, GroupScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = getMoimScheduleUserDtoMap(
			moimAndUsers);
		Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap
			= getMoimScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
		addMoimSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
		return result;
	}

	private static List<GroupScheduleResponse.MoimScheduleDto> getMoimScheduleDtos(
		List<Schedule> indivisualsSchedules,
		List<MoimAndUser> moimAndUsers
	) {
		Map<User, Integer> usersColor = moimAndUsers.stream().collect(
			Collectors.toMap(
				MoimAndUser::getUser, MoimAndUser::getColor
			));
		return indivisualsSchedules.stream()
			.map((schedule -> toMoimScheduleDto(schedule, usersColor.get(schedule.getUser()))))
			.collect(Collectors.toList());
	}

	private static Map<User, GroupScheduleResponse.MoimScheduleUserDto> getMoimScheduleUserDtoMap(
		List<MoimAndUser> moimAndUsers
	) {
		return moimAndUsers.stream()
			.collect(Collectors.toMap(
				MoimAndUser::getUser,
				(MoimScheduleResponseConverter::toMoimScheduleUserDto)
			));
	}

	private static GroupScheduleResponse.MoimScheduleUserDto toMoimScheduleUserDto(MoimAndUser moimAndUser) {
		return GroupScheduleResponse.MoimScheduleUserDto
			.builder()
			.userId(moimAndUser.getUser().getId())
			.userName(moimAndUser.getUser().getName())
			.color(moimAndUser.getColor())
			.build();
	}

	private static Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> getMoimScheduleMappingUserDtoMap(
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

	private static void addMoimSchedulesToResult(List<MoimAndUser> moimAndUsers,
		List<GroupScheduleResponse.MoimScheduleDto> result,
		Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap) {
		for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
			GroupScheduleResponse.MoimScheduleDto moimScheduleDto = toMoimScheduleDto(moimSchedule);
			moimScheduleDto.setUsers(
				moimScheduleMappingUserDtoMap.get(moimSchedule),
				moimSchedule.getMoim() == moimAndUsers.get(0).getMoim(),
				moimSchedule.getMoimMemo() != null
			);
			result.add(moimScheduleDto);
		}
	}

	public static GroupScheduleResponse.MoimScheduleDto toMoimScheduleDto(Schedule schedule, Integer color) {
		return new GroupScheduleResponse.MoimScheduleDto(
			schedule.getName(),
			schedule.getPeriod().getStartDate(),
			schedule.getPeriod().getEndDate(),
			schedule.getPeriod().getDayInterval(),
			schedule.getUser().getId(),
			schedule.getUser().getName(),
			color
		);
	}

	public static GroupScheduleResponse.MoimScheduleDto toMoimScheduleDto(MoimSchedule moimSchedule) {
		return new GroupScheduleResponse.MoimScheduleDto(
			moimSchedule.getName(),
			moimSchedule.getPeriod().getStartDate(),
			moimSchedule.getPeriod().getEndDate(),
			moimSchedule.getPeriod().getDayInterval(),
			moimSchedule.getMoim().getId(),
			moimSchedule.getId(),
			moimSchedule.getLocation().getX(),
			moimSchedule.getLocation().getY(),
			moimSchedule.getLocation().getLocationName(),
			moimSchedule.getLocation().getKakaoLocationId()
		);
	}

}
