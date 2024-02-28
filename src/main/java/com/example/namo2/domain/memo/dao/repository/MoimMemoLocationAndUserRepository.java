package com.example.namo2.domain.memo.dao.repository;

import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoimMemoLocationAndUserRepository extends JpaRepository<MoimMemoLocationAndUser, Long> {
    @Modifying
    @Query("delete from MoimMemoLocationAndUser lau where lau.moimMemoLocation = :moimMemoLocation")
    void deleteMoimMemoLocationAndUserByMoimMemoLocation(MoimMemoLocation moimMemoLocation);

    @Modifying
    @Query("delete from MoimMemoLocationAndUser lau where lau.moimMemoLocation in :moimMemoLocation")
    void deleteMoimMemoLocationAndUserByMoimMemoLocation(List<MoimMemoLocation> moimMemoLocation);
}