package com.namo.spring.db.mysql.domains.individual.repository.schedule;

import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    @Query("select s from Schedule s join fetch s.user where s.user in :users and s.category.share = true ")
    List<Schedule> findSchedulesByUsers(@Param("users") List<User> users);

    List<Schedule> findAllByUser(User user);

    List<Schedule> findAllScheduleByUser(User user);

    @Query("select s from Schedule s join fetch s.images where s.user = :user and s.hasDiary is TRUE")
    List<Schedule> findAllScheduleDiary(User user);
}
