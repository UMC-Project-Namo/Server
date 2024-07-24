package com.namo.spring.db.mysql.domains.individual.repository.schedule;

import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.dto.MoimScheduleProjection;
import com.namo.spring.db.mysql.domains.individual.dto.ScheduleProjection;
import com.namo.spring.db.mysql.domains.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {

    Slice<ScheduleProjection.DiaryDto> findScheduleDiaryByMonth(User user, LocalDateTime startDate,
                                                                LocalDateTime endTime, Pageable pageable);

    List<ScheduleProjection.DiaryByUserDto> findAllScheduleDiary(User user);

    Schedule findOneScheduleAndImages(Long scheduleId);

    List<MoimScheduleProjection.ScheduleDto> findMoimSchedulesByUserId(User user, LocalDateTime startDate,
                                                                       LocalDateTime endDate);

    List<ScheduleProjection.ScheduleDto> findPersonalSchedulesByUserId(User user, LocalDateTime startDate,
                                                                       LocalDateTime endDate);
}
