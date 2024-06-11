package com.namo.spring.application.external.domain.group.repository.group;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.application.external.domain.group.domain.Moim;

public interface MoimRepository extends JpaRepository<Moim, Long>, MoimRepositoryCustom {
	@Query("select m from Moim m join m.moimAndUsers where m.id = :moimId")
	Optional<Moim> findMoimWithMoimAndUsersByMoimId(Long moimId);
}
