package com.namo.spring.db.mysql.domains.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
}
