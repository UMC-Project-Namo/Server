package com.example.namo2.domain.group.dao.repository.schedule;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.namo2.domain.group.domain.MoimSchedule;
import com.example.namo2.domain.group.domain.MoimScheduleAndUser;

import com.example.namo2.domain.user.domain.User;

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
