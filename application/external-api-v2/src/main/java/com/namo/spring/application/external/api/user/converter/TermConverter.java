package com.namo.spring.application.external.api.user.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.api.user.dto.MemberResponse;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.type.Content;

public class TermConverter {
	public static List<Term> toTerms(MemberRequest.TermDto termDto, Member member) {
		List<Term> terms = new ArrayList<>();
		Term termOfUse = toTerm(Content.TERMS_OF_USE, termDto.getIsCheckTermOfUse(), member);
		Term termOfPersonal
			= toTerm(Content.PERSONAL_INFORMATION_COLLECTION, termDto.getIsCheckPersonalInformationCollection(),
			member);
		terms.add(termOfUse);
		terms.add(termOfPersonal);
		return terms;
	}

	private static Term toTerm(Content content, Boolean agree, Member member) {
		return Term.builder()
			.user(member)
			.content(content)
			.agree(agree)
			.build();
	}

	public static List<MemberResponse.TermsDto> toTerms(List<Term> terms) {
		List<MemberResponse.TermsDto> termsDto = new ArrayList<>();
		for (Content content : Content.values()) {
			Optional<Term> term = terms.stream()
				.filter(t -> t.getContent().equals(content))
				.findFirst();

			if (term.isEmpty()) {
				termsDto.add(getTermsDto(content, false));
			} else {
				termsDto.add(getTermsDto(term.get().getContent(), term.get().isAgree()));
			}
		}
		return termsDto;
	}

	private static MemberResponse.TermsDto getTermsDto(Content content, boolean isCheck) {
		return MemberResponse.TermsDto.builder()
			.content(content.name())
			.isCheck(isCheck)
			.build();
	}
}
