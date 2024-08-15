package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.core.infra.common.properties.ImageUrlProperties;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.namo.spring.application.external.api.schedule.converter.ScheduleConverter.toMeetingSchedule;
import static com.namo.spring.application.external.api.schedule.converter.ScheduleConverter.toPersonalSchedule;

@Component
@RequiredArgsConstructor
public class ScheduleMaker {
    private static final int PERSONAL_SCHEDULE_TYPE = ScheduleType.PERSONAL.getValue();
    private static final int MEETING_SCHEDULE_TYPE = ScheduleType.MEETING.getValue();

    private final ScheduleService scheduleService;
    private final ImageUrlProperties imageUrlProperties;
    private final FileUtils fileUtils;

    @Transactional
    public Schedule createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Period period) {
        Location location = Location.of(dto.getLongitude(), dto.getLatitude(), dto.getLocationName(), dto.getKakaoLocationId());
        Schedule schedule = toPersonalSchedule(dto.getTitle(), period, location);
        return scheduleService.createSchedule(schedule);
    }

    @Transactional
    public Schedule createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, Period period, MultipartFile image) {
        Location location = Location.of(dto.getLongitude(), dto.getLatitude(), dto.getLocationName(), dto.getKakaoLocationId());

        String url = imageUrlProperties.getMeeting();
        if (image != null && !image.isEmpty()) {
            url = fileUtils.uploadImage(image, FilePath.MEETING_PROFILE_IMG);
        }

        Schedule schedule = toMeetingSchedule(dto.getTitle(), period, location, url);
        return scheduleService.createSchedule(schedule);
    }

    private void validateScheduleType(int scheduleType) {

    }
}
