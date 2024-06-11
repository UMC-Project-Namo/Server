package com.namo.spring.application.external.domain.individual.repository.image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.application.external.domain.individual.domain.Image;
import com.namo.spring.application.external.domain.individual.domain.Schedule;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
	Optional<List<Image>> findAllBySchedule(Schedule schedule);

}
