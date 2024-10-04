package com.namo.spring.application.external.api.schedule.dto.interfaces;

import java.time.LocalDateTime;

public interface PeriodDtoInterface {
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}
