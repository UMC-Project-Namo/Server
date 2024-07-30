package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.application.external.api.group.dto.MeetingScheduleResponse;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.user.domain.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeetingScheduleResponseConverter {
    private MeetingScheduleResponseConverter() {
        throw new IllegalStateException("Util Classes");
    }

    public static List<MeetingScheduleResponse.GetMonthlyMeetingScheduleDto> toGetMonthlyMeetingScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            List<MoimAndUser> moimAndUsers
    ) {
        List<MeetingScheduleResponse.GetMonthlyMeetingScheduleDto> result = getGetMonthlyMeetingScheduleDtos(individualsSchedules,
                moimAndUsers);

        Map<User, MeetingScheduleResponse.MeetingScheduleUserDto> moimScheduleUserDtoMap = getMeetingScheduleUserDtoMap(
                moimAndUsers);
        Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> moimScheduleMappingUserDtoMap
                = getMeetingScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
        addMonthlyMeetingSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
        return result;
    }

    public static List<MeetingScheduleResponse.GetAllMeetingScheduleDto> toGetAllMeetingScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            List<MoimAndUser> moimAndUsers
    ) {
        List<MeetingScheduleResponse.GetAllMeetingScheduleDto> result = getAllMeetingScheduleDtos(individualsSchedules,
                moimAndUsers);

        Map<User, MeetingScheduleResponse.MeetingScheduleUserDto> moimScheduleUserDtoMap = getMeetingScheduleUserDtoMap(
                moimAndUsers);
        Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> moimScheduleMappingUserDtoMap
                = getMeetingScheduleMappingUserDtoMap(moimScheduleAndUsers, moimScheduleUserDtoMap);
        addAllMeetingSchedulesToResult(moimAndUsers, result, moimScheduleMappingUserDtoMap);
        return result;
    }

    private static List<MeetingScheduleResponse.GetMonthlyMeetingScheduleDto> getGetMonthlyMeetingScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimAndUser> moimAndUsers
    ) {
        Map<User, Integer> usersColor = moimAndUsers.stream().collect(
                Collectors.toMap(
                        MoimAndUser::getUser, MoimAndUser::getColor
                ));
        return individualsSchedules.stream()
                .map((schedule -> toGetMonthlyMeetingScheduleDto(schedule, usersColor.get(schedule.getUser()))))
                .collect(Collectors.toList());
    }

    private static List<MeetingScheduleResponse.GetAllMeetingScheduleDto> getAllMeetingScheduleDtos(
            List<Schedule> individualsSchedules,
            List<MoimAndUser> moimAndUsers
    ) {
        Map<User, Integer> usersColor = moimAndUsers.stream().collect(
                Collectors.toMap(
                        MoimAndUser::getUser, MoimAndUser::getColor
                ));
        return individualsSchedules.stream()
                .map((schedule -> toGetAllMeetingScheduleDto(schedule, usersColor.get(schedule.getUser()))))
                .collect(Collectors.toList());
    }

    private static Map<User, MeetingScheduleResponse.MeetingScheduleUserDto> getMeetingScheduleUserDtoMap(
            List<MoimAndUser> moimAndUsers
    ) {
        return moimAndUsers.stream()
                .collect(Collectors.toMap(
                        MoimAndUser::getUser,
                        (MeetingScheduleResponseConverter::toMeetingScheduleUserDto)
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

    private static void addMonthlyMeetingSchedulesToResult(List<MoimAndUser> moimAndUsers,
                                                           List<MeetingScheduleResponse.GetMonthlyMeetingScheduleDto> result,
                                                           Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> moimScheduleMappingUserDtoMap) {
        for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
            boolean isCurGroupSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
            MeetingScheduleResponse.GetMonthlyMeetingScheduleDto groupScheduleDto =
                    MeetingScheduleResponse.GetMonthlyMeetingScheduleDto.fromMoimSchedule(moimSchedule,
                            moimScheduleMappingUserDtoMap.get(moimSchedule),
                            isCurGroupSchedule);
            result.add(groupScheduleDto);
        }
    }

    private static void addAllMeetingSchedulesToResult(List<MoimAndUser> moimAndUsers,
                                                       List<MeetingScheduleResponse.GetAllMeetingScheduleDto> result,
                                                       Map<MoimSchedule, List<MeetingScheduleResponse.MeetingScheduleUserDto>> moimScheduleMappingUserDtoMap) {
        for (MoimSchedule moimSchedule : moimScheduleMappingUserDtoMap.keySet()) {
            boolean isCurGroupSchedule = moimSchedule.getMoim() == moimAndUsers.get(0).getMoim();
            MeetingScheduleResponse.GetAllMeetingScheduleDto groupScheduleDto =
                    MeetingScheduleResponse.GetAllMeetingScheduleDto.fromMoimSchedule(moimSchedule,
                            moimScheduleMappingUserDtoMap.get(moimSchedule),
                            isCurGroupSchedule);
            result.add(groupScheduleDto);
        }
    }

    public static MeetingScheduleResponse.GetMonthlyMeetingScheduleDto toGetMonthlyMeetingScheduleDto(Schedule schedule, Integer color) {
        List<MeetingScheduleResponse.MeetingScheduleUserDto> users = List.of(
                toMeetingScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
        return MeetingScheduleResponse.GetMonthlyMeetingScheduleDto.fromSchedule(schedule, users);
    }

    public static MeetingScheduleResponse.GetAllMeetingScheduleDto toGetAllMeetingScheduleDto(Schedule schedule, Integer color) {
        List<MeetingScheduleResponse.MeetingScheduleUserDto> users = List.of(
                toMeetingScheduleUserDto(schedule.getUser().getId(), schedule.getUser().getName(), color));
        return MeetingScheduleResponse.GetAllMeetingScheduleDto.fromSchedule(schedule, users);
    }

    public static MeetingScheduleResponse.MeetingScheduleUserDto toMeetingScheduleUserDto(Long userId, String userName,
                                                                                          Integer color) {
        return MeetingScheduleResponse.MeetingScheduleUserDto.builder()
                .userId(userId)
                .userName(userName)
                .color(color)
                .build();
    }

}
