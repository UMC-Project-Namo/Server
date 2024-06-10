package com.example.namo2.domain.individual.dao.repository.image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.individual.domain.Image;
import com.example.namo2.domain.individual.domain.Schedule;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
	Optional<List<Image>> findAllBySchedule(Schedule schedule);

}
