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

public class MoimScheduleResponseConverter {
    private MoimScheduleResponseConverter() {
        throw new IllegalStateException("Util Classes");
    }

    public static List<GroupScheduleResponse.MoimScheduleDto> toMoimScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            List<MoimAndUser> moimAndUsers
    ) {
        List<GroupScheduleResponse.MoimScheduleDto> result = getMoimScheduleDtos(individualsSchedules, moimAndUsers);

        Map<User, GroupScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtoMap = getMoimScheduleUserDtoMap(
                moimAndUsers);
        Map<MoimSchedule, List<GroupScheduleResponse.MoimScheduleUserDto>> moimScheduleMappingUserDtoMap
                = getMoimScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
        addMoimSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
        return result;
    }

    private static List<GroupScheduleResponse.MoimScheduleDto> getMoimScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimAndUser> moimAndUsers
    ) {
        Map<User, Integer> usersColor = moimAndUsers.stream().collect(
                Collectors.toMap(
                        MoimAndUser::getUser, MoimAndUser::getColor
                ));
        return individualsSchedules.stream()
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
            boolean isCurMoimSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
            GroupScheduleResponse.MoimScheduleDto moimScheduleDto =
                    GroupScheduleResponse.MoimScheduleDto.fromMoimSchedule(moimSchedule, moimScheduleMappingUserDtoMap.get(moimSchedule),
                            isCurMoimSchedule);
            result.add(moimScheduleDto);
        }
    }

    public static GroupScheduleResponse.MoimScheduleDto toMoimScheduleDto(Schedule schedule, Integer color) {
        List<GroupScheduleResponse.MoimScheduleUserDto> users = List.of(
                toMoimScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
        return GroupScheduleResponse.MoimScheduleDto.fromSchedule(schedule, users);
    }

    public static GroupScheduleResponse.MoimScheduleUserDto toMoimScheduleUserDto(Long userId, String userName,
                                                                                  Integer color) {
        return GroupScheduleResponse.MoimScheduleUserDto.builder()
                .userId(userId)
                .userName(userName)
                .color(color)
                .build();
    }

}
