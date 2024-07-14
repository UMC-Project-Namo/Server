package com.namo.spring.db.mysql.domains.schedule.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.entity.PersonalSchedule;
import com.namo.spring.db.mysql.domains.schedule.repository.PersonalScheduleRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class PersonalScheduleService {
	private final PersonalScheduleRepository personalScheduleRepository;

	@Transactional
	public PersonalSchedule createPersonalSchedule(PersonalSchedule personalSchedule) {
		return personalScheduleRepository.save(personalSchedule);
	}

	@Transactional(readOnly = true)
	public Optional<PersonalSchedule> getPersonalSchedule(Long id) {
		return personalScheduleRepository.findById(id);
	}

	@Transactional
	public void deletePersonalSchedule(Long id) {
		personalScheduleRepository.deleteById(id);
	}

}
