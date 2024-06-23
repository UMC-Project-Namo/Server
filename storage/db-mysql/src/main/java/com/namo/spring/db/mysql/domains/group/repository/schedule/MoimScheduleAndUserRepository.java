package com.namo.spring.db.mysql.domains.group.repository.schedule;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.user.domain.User;

public interface MoimScheduleAndUserRepository
	extends JpaRepository<MoimScheduleAndUser, Long>, MoimScheduleAndUserRepositoryCustom {
	@Modifying
	@Query("delete from MoimScheduleAndUser mau"
		+ " where mau.moimSchedule = :moimSchedule")
	void deleteMoimScheduleAndUserByMoimSchedule(MoimSchedule moimSchedule);

	Optional<MoimScheduleAndUser> findMoimScheduleAndUserByMoimScheduleAndUser(MoimSchedule moimSchedule, User user);

	@Query("select msu from MoimScheduleAndUser msu where msu.moimSchedule = :moimSchedule")
	List<MoimScheduleAndUser> findMoimScheduleAndUserByMoimSchedule(@Param("moimSchedule") MoimSchedule moimSchedule);

	List<MoimScheduleAndUser> findAllByUser(User user);
}
