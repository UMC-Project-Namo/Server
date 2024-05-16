package com.example.namo2.domain.group.dao.repository.diary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.namo2.domain.group.domain.MoimMemo;

import com.example.namo2.domain.group.domain.MoimSchedule;

public interface MoimMemoRepository extends JpaRepository<MoimMemo, Long> {
	@Query(value = "select distinct mm"
		+ " from MoimMemo mm"
		+ " join fetch mm.moimSchedule ms"
		+ " join fetch ms.moimScheduleAndUsers msu" + " join fetch msu.user" + " where ms = :moimSchedule")
	MoimMemo findMoimMemoAndUsersByMoimSchedule(@Param("moimSchedule") MoimSchedule moimSchedule);

	Optional<MoimMemo> findMoimMemoByMoimSchedule(MoimSchedule moimSchedule);

	@Query("select mm"
		+ " from MoimMemo mm"
		+ " left join fetch mm.moimMemoLocations"
		+ " where mm.moimSchedule =:moimSchedule")
	MoimMemo findMoimMemoAndLocationsByMoimSchedule(MoimSchedule moimSchedule);

	@Query("select mm"
		+ " from MoimMemo mm"
		+ " left join fetch mm.moimMemoLocations"
		+ " where mm.moimSchedule.id =:moimScheduleId")
	Optional<MoimMemo> findMoimMemoAndLocationsByMoimSchedule(Long moimScheduleId);
}
