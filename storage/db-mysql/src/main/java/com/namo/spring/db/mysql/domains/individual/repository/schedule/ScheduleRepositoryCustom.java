package com.namo.spring.db.mysql.domains.individual.repository.schedule;

import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.dto.DiaryResponse;
import com.namo.spring.db.mysql.domains.individual.dto.ScheduleResponse;
import com.namo.spring.db.mysql.domains.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
	List<ScheduleResponse.GetScheduleDto> findSchedulesByUserId(User user, LocalDateTime startDate,
																LocalDateTime endDate);

	Slice<ScheduleResponse.DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate,
																 LocalDateTime endTime, Pageable pageable);

	List<DiaryResponse.GetDiaryByUserDto> findAllScheduleDiary(User user);

	Schedule findOneScheduleAndImages(Long scheduleId);

	List<ScheduleResponse.GetScheduleDto> findMoimSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate);
}
