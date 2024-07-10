package com.namo.spring.application.external.api.user.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.dto.UserResponse;
import com.namo.spring.db.mysql.domains.user.domain.Term;
import com.namo.spring.db.mysql.domains.user.type.Content;
import com.namo.spring.db.mysql.domains.user.domain.User;

public class TermConverter {
	public static List<Term> toTerms(UserRequest.TermDto termDto, User user) {
		List<Term> terms = new ArrayList<>();
		Term termOfUse = toTerm(Content.TERMS_OF_USE, termDto.getIsCheckTermOfUse(), user);
		Term termOfPersonal
			= toTerm(Content.PERSONAL_INFORMATION_COLLECTION, termDto.getIsCheckPersonalInformationCollection(), user);
		terms.add(termOfUse);
		terms.add(termOfPersonal);
		return terms;
	}

	private static Term toTerm(Content content, Boolean isCheck, User user) {
		return Term.builder()
			.user(user)
			.content(content)
			.isCheck(isCheck)
			.build();
	}

	public static List<UserResponse.TermsDto> toTerms(List<Term> terms) {
		List<UserResponse.TermsDto> termsDto = new ArrayList<>();
		for (Content content : Content.values()) {
			Optional<Term> term = terms.stream()
				.filter(t -> t.getContent().equals(content))
				.findFirst();

			if (term.isEmpty()) {
				termsDto.add(getTermsDto(content, false));
			} else {
				termsDto.add(getTermsDto(term.get().getContent(), term.get().getIsCheck()));
			}
		}
		return termsDto;
	}

	private static UserResponse.TermsDto getTermsDto(Content content, boolean isCheck) {
		return UserResponse.TermsDto.builder()
			.content(content.name())
			.isCheck(isCheck)
			.build();
	}
}
