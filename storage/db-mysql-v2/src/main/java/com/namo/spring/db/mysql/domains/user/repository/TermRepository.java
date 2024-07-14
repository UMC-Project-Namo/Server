package com.namo.spring.db.mysql.domains.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.user.entity.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
	Optional<List<Term>> findByUserId(Long userId);
}
