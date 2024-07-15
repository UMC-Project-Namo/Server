package com.namo.spring.db.mysql.domains.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.repository.TermRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class TermService {
	private final TermRepository termRepository;

	@Transactional
	public Term createTerm(Term term) {
		return termRepository.save(term);
	}

	@Transactional(readOnly = true)
	public List<Term> readTermListByUserId(Long userId) {
		return termRepository.findByUserId(userId);
	}

	@Transactional
	public void deleteTerm(Long termId) {
		termRepository.deleteById(termId);
	}
}
