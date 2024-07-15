package com.namo.spring.db.mysql.domains.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
