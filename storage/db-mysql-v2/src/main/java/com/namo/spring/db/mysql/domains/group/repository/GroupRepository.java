package com.namo.spring.db.mysql.domains.group.repository;

import com.namo.spring.db.mysql.domains.group.entity.Group;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM Group g JOIN FETCH g.groupUsers WHERE g.code = :code")
    Optional<Group> findGroupWithGroupUsersByCode(@Param("code") String code);

    @Query("SELECT g FROM Group g JOIN FETCH g.groupUsers where g.id = :id")
    Optional<Group> findGroupWithGroupUsers(@Param("id") Long id);

}
