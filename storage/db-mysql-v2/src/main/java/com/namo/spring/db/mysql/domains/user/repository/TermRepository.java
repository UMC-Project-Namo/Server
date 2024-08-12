package com.namo.spring.db.mysql.domains.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.user.entity.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
	List<Term> findByMemberId(Long memberId);
}
