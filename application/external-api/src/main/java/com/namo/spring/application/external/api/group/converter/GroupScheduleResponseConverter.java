package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.application.external.api.group.dto.GroupScheduleResponse;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.user.domain.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupScheduleResponseConverter {
    private GroupScheduleResponseConverter() {
        throw new IllegalStateException("Util Classes");
    }

    public static List<GroupScheduleResponse.GetMonthlyGroupScheduleDto> toGetMonthlyGroupScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            List<MoimAndUser> moimAndUsers
    ) {
        List<GroupScheduleResponse.GetMonthlyGroupScheduleDto> result = getMonthlyGroupScheduleDtos(individualsSchedules, moimAndUsers);

        Map<User, GroupScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = getGroupScheduleUserDtoMap(
                moimAndUsers);
        Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap
                = getGroupScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
        addGetMonthlyGroupSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
        return result;
    }

    public static List<GroupScheduleResponse.GetAllGroupScheduleDto> toGetAllGroupScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            List<MoimAndUser> moimAndUsers
    ) {
        List<GroupScheduleResponse.GetAllGroupScheduleDto> result = getAllGroupScheduleDtos(individualsSchedules, moimAndUsers);

        Map<User, GroupScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = getGroupScheduleUserDtoMap(
                moimAndUsers);
        Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap
                = getGroupScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
        addGetAllGroupSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
        return result;
    }


    private static List<GroupScheduleResponse.GetMonthlyGroupScheduleDto> getMonthlyGroupScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimAndUser> moimAndUsers
    ) {
        Map<User, Integer> usersColor = moimAndUsers.stream().collect(
                Collectors.toMap(
                        MoimAndUser::getUser, MoimAndUser::getColor
                ));
        return individualsSchedules.stream()
                .map((schedule -> toGetMonthlyGroupScheduleDto(schedule, usersColor.get(schedule.getUser()))))
                .collect(Collectors.toList());
    }

    private static List<GroupScheduleResponse.GetAllGroupScheduleDto> getAllGroupScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimAndUser> moimAndUsers
    ) {
        Map<User, Integer> usersColor = moimAndUsers.stream().collect(
                Collectors.toMap(
                        MoimAndUser::getUser, MoimAndUser::getColor
                ));
        return individualsSchedules.stream()
                .map((schedule -> toGetAllGroupScheduleDto(schedule, usersColor.get(schedule.getUser()))))
                .collect(Collectors.toList());
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


    private static GroupScheduleResponse.MoimScheduleUserDto toGroupScheduleUserDto(MoimAndUser moimAndUser) {
        return GroupScheduleResponse.MoimScheduleUserDto
                .builder()
                .userId(moimAndUser.getUser().getId())
                .userName(moimAndUser.getUser().getName())
                .color(moimAndUser.getColor())
                .build();
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


    private static void addGetMonthlyGroupSchedulesToResult(List<MoimAndUser> moimAndUsers,
                                                            List<GroupScheduleResponse.GetMonthlyGroupScheduleDto> result,
                                                            Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap) {
        for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
            boolean isCurGroupSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
            GroupScheduleResponse.GetMonthlyGroupScheduleDto getMonthlyGroupScheduleDto =
                    GroupScheduleResponse.GetMonthlyGroupScheduleDto.fromMoimSchedule(moimSchedule,
                            moimScheduleMappingUserDtoMap.get(moimSchedule),
                            isCurGroupSchedule);
            result.add(getMonthlyGroupScheduleDto);
        }
    }

    private static void addGetAllGroupSchedulesToResult(List<MoimAndUser> moimAndUsers,
                                                        List<GroupScheduleResponse.GetAllGroupScheduleDto> result,
                                                        Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap) {
        for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
            boolean isCurGroupSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
            GroupScheduleResponse.GetAllGroupScheduleDto getMonthlyGroupScheduleDto =
                    GroupScheduleResponse.GetAllGroupScheduleDto.fromMoimSchedule(moimSchedule,
                            moimScheduleMappingUserDtoMap.get(moimSchedule),
                            isCurGroupSchedule);
            result.add(getMonthlyGroupScheduleDto);
        }
    }


    public static GroupScheduleResponse.GetMonthlyGroupScheduleDto toGetMonthlyGroupScheduleDto(Schedule schedule, Integer color) {
        List<GroupScheduleResponse.MoimScheduleUserDto> users = List.of(
                toGroupScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
        return GroupScheduleResponse.GetMonthlyGroupScheduleDto.fromSchedule(schedule, users);
    }

    public static GroupScheduleResponse.GetAllGroupScheduleDto toGetAllGroupScheduleDto(Schedule schedule, Integer color) {
        List<GroupScheduleResponse.MoimScheduleUserDto> users = List.of(
                toGroupScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
        return GroupScheduleResponse.GetAllGroupScheduleDto.fromSchedule(schedule, users);
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
