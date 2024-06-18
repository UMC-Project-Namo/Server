package com.namo.spring.application.external.domain.group.repository.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.namo.spring.application.external.domain.group.domain.MoimScheduleAndUser;

import com.namo.spring.db.mysql.domains.user.domain.User;

public interface MoimScheduleAndUserRepositoryCustom {
	List<MoimScheduleAndUser> findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(LocalDateTime startDate,
		LocalDateTime endDate, List<User> users);

	List<MoimScheduleAndUser> findMoimScheduleMemoByMonthPaging(User user, List<LocalDateTime> dates,
		Pageable pageable);
}
