package com.namo.spring.application.external.api.user.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.TermService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TermManageService {

	private final TermService termService;

	public void createOrUpdateTerm(List<Term> terms) {
		for (Term term : terms) {
			validateTermAgreement(term);
			termService.readTerm(term.getContent(), term.getMember())
				.ifPresentOrElse(
					Term::update,
					() -> termService.createTerm(term)
				);
		}
	}

	private void validateTermAgreement(Term term) {
		if (!term.isAgree())
			throw new MemberException(ErrorStatus.NOT_CHECK_TERM_ERROR);
	}

}
