package com.namo.spring.db.mysql.domains.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.group.entity.GroupUser;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
}
