package com.namo.spring.db.mysql.domains.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.group.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
