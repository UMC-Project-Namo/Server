package com.namo.spring.db.mysql.domains.group.repository.diary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocation;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationImg;

public interface MoimMemoLocationImgRepository extends JpaRepository<MoimMemoLocationImg, Long> {
	@Modifying
	@Query("delete from MoimMemoLocationImg mli where mli.moimMemoLocation = :moimMemoLocation")
	void deleteMoimMemoLocationImgByMoimMemoLocation(MoimMemoLocation moimMemoLocation);

	@Modifying
	@Query("delete from MoimMemoLocationImg mli where mli.moimMemoLocation in :moimMemoLocations")
	void deleteMoimMemoLocationImgByMoimMemoLocation(
		@Param("moimMemoLocations") List<MoimMemoLocation> moimMemoLocation);

	@Query("select mli "
		+ "from MoimMemoLocationImg mli "
		+ "where mli.moimMemoLocation in :moimMemoLocations")
	List<MoimMemoLocationImg> findMoimMemoLocationImgsByMoimMemoLocations(List<MoimMemoLocation> moimMemoLocations);
}
