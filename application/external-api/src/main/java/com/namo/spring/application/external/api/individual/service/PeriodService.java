package com.namo.spring.application.external.api.individual.service;

import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.individual.type.Period;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.IndividualException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeriodService {

	public void checkValidDate(Period period) {
		if (period.getStartDate().isAfter(period.getEndDate())) {
			throw new IndividualException(ErrorStatus.INVALID_DATE);
		}
	}
}
