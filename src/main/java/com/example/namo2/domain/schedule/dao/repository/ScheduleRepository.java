package com.example.namo2.domain.schedule.dao.repository;

import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    @Query("select s from Schedule s join fetch s.user where s.user in :users and s.category.share = true ")
    List<Schedule> findSchedulesByUsers(@Param("users") List<User> users);
}