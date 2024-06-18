package com.namo.spring.db.mysql.domains.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.user.domain.Term;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.mysql.domains.user.type.Content;

public interface TermRepository extends JpaRepository<Term, Long> {
	Optional<Term> findTermByContentAndUser(Content content, User user);

	List<Term> findTermsByUser(User user);
}
