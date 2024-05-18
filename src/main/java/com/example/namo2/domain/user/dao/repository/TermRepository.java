package com.example.namo2.domain.user.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.user.domain.constant.Content;
import com.example.namo2.domain.user.domain.Term;
import com.example.namo2.domain.user.domain.User;

public interface TermRepository extends JpaRepository<Term, Long> {
	Optional<Term> findTermByContentAndUser(Content content, User user);
}
