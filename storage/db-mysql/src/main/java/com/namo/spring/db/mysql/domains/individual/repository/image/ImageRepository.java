package com.namo.spring.db.mysql.domains.individual.repository.image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
	Optional<List<Image>> findAllBySchedule(Schedule schedule);

}
