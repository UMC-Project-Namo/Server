package com.namo.spring.db.mysql.domains.individual.repository.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.dto.DiaryProjection;
import com.namo.spring.db.mysql.domains.individual.dto.ScheduleProjection;
import com.namo.spring.db.mysql.domains.user.domain.User;

public interface ScheduleRepositoryCustom {
	List<ScheduleProjection.ScheduleDto> findSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate);

	Slice<ScheduleProjection.DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate,
		LocalDateTime endTime, Pageable pageable);

	List<DiaryProjection.DiaryByUserDto> findAllScheduleDiary(User user);

	Schedule findOneScheduleAndImages(Long scheduleId);

	List<ScheduleProjection.ScheduleDto> findMoimSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate);
}
