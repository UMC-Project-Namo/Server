package com.example.namo2.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Converter {
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
			throw new BaseException(BaseResponseStatus.INVALID_FORMAT_FAILURE);
		}
		for (String element : dateInformation) {
			if (!element.matches("\\d+")) {
				throw new BaseException(BaseResponseStatus.INVALID_FORMAT_FAILURE);
			}
		}
	}

	private void validateMonth(int year, int month) {
		if (year < 0 || month > 12 || month <= 0) {
			throw new BaseException(BaseResponseStatus.INVALID_FORMAT_FAILURE);
		}
	}
}
