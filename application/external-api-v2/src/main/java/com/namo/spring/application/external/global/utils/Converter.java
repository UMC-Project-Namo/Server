package com.namo.spring.application.external.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.UtilsException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Converter {
	public List<Integer> splitYearMonth(String yearAndMonth) {
		String[] dateInformation = yearAndMonth.split(",");
		validateFormat(dateInformation);

		int year = Integer.parseInt(dateInformation[0]);
		int month = Integer.parseInt(dateInformation[1]);
		validateMonth(year, month);

		return List.of(year, month);
	}

	public List<LocalDateTime> convertLongToLocalDateTime(String yearAndMonth) {
		String[] dateInformation = yearAndMonth.split(",");
		validateFormat(dateInformation);

		int year = Integer.parseInt(dateInformation[0]);
		int month = Integer.parseInt(dateInformation[1]);
		validateMonth(year, month);
		LocalDateTime startMonth = LocalDate.of(year, month, 1).atStartOfDay();
		LocalDateTime localDateTime = startMonth.plusMonths(1L);
		return List.of(startMonth, localDateTime);
	}

	private void validateFormat(String[] dateInformation) {
		if (dateInformation.length != 2) {
			throw new UtilsException(ErrorStatus.INVALID_FORMAT_FAILURE);
		}
		for (String element : dateInformation) {
			if (!element.matches("\\d+")) {
				throw new UtilsException(ErrorStatus.INVALID_FORMAT_FAILURE);
			}
		}
	}

	private void validateMonth(int year, int month) {
		if (year < 0 || month > 12 || month <= 0) {
			throw new UtilsException(ErrorStatus.INVALID_FORMAT_FAILURE);
		}
	}
}
