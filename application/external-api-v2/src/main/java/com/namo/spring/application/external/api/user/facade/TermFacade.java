package com.namo.spring.application.external.api.user.facade;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.TermConverter;
import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.api.user.service.TermManageService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.exception.TermException;
import com.namo.spring.db.mysql.domains.user.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermFacade {

	private final MemberService memberService;
	private final TermManageService termManage;

	@Transactional(readOnly = false)
	public void termAgreement(MemberRequest.TermDto termDto, Long memberId) {
		Member member = memberService.readMember(memberId)
			.orElseThrow(() -> new TermException(ErrorStatus.NOT_FOUND_USER_FAILURE));
		List<Term> terms = TermConverter.toTerms(termDto, member);
		termManage.createOrUpdateTerm(terms);
	}
}
