package com.namo.spring.application.external.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.application.external.domain.user.domain.Term;
import com.namo.spring.application.external.domain.user.domain.User;
import com.namo.spring.application.external.domain.user.domain.constant.Content;

public interface TermRepository extends JpaRepository<Term, Long> {
	Optional<Term> findTermByContentAndUser(Content content, User user);

	List<Term> findTermsByUser(User user);
}
