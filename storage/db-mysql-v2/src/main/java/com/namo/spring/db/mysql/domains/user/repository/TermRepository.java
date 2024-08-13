package com.namo.spring.db.mysql.domains.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.type.Content;

public interface TermRepository extends JpaRepository<Term, Long> {

	Optional<Term> findTermByContentAndMember(Content content, Member user);

	List<Term> findByMemberId(Long memberId);

	List<Term> findTermsByMember(Member member);
}
