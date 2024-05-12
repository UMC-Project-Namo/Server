package com.example.namo2.domain.group.application.impl;

import com.example.namo2.domain.group.dao.repository.diary.MoimMemoLocationAndUserRepository;
import com.example.namo2.domain.group.dao.repository.diary.MoimMemoLocationImgRepository;
import com.example.namo2.domain.group.dao.repository.diary.MoimMemoLocationRepository;
import com.example.namo2.domain.group.domain.*;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoimMemoLocationService {
    private final MoimMemoLocationRepository moimMemoLocationRepository;
    private final MoimMemoLocationAndUserRepository moimMemoLocationAndUserRepository;
    private final MoimMemoLocationImgRepository moimMemoLocationImgRepository;

    public MoimMemoLocation createMoimMemoLocation(MoimMemoLocation moimMemoLocation, MoimMemo moimMemo) {
        if (moimMemo.isFullLocationSize()) {
            throw new BaseException(BaseResponseStatus.MOIM_MEMO_IS_FULL_ERROR);
        }
        return moimMemoLocationRepository.save(moimMemoLocation);
    }

    public List<MoimMemoLocationAndUser> createMoimMemoLocationAndUsers(
            List<MoimMemoLocationAndUser> moimMemoLocations) {
        return moimMemoLocationAndUserRepository.saveAll(moimMemoLocations);
    }

    public MoimMemoLocationImg createMoimMemoLocationImg(MoimMemoLocationImg moimMemoLocationImg) {
        return moimMemoLocationImgRepository.save(moimMemoLocationImg);
    }

    public MoimMemoLocation getMoimMemoLocationWithImgs(Long memoLocationId) {
        return moimMemoLocationRepository.findMoimMemoLocationWithImgsById(memoLocationId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOIM_MEMO_LOCATION_FAILURE));
    }

    public void removeMoimMemoLocationAndUsers(MoimMemoLocation moimMemoLocation) {
        moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
    }

    public void removeMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocation) {
        moimMemoLocationAndUserRepository.deleteMoimMemoLocationAndUserByMoimMemoLocation(moimMemoLocation);
    }

    public void removeMoimMemoLocationAndUsersByUser(User user) {
        moimMemoLocationAndUserRepository.deleteAllByUser(user);
    }

    public void removeMoimMemoLocationImgs(MoimMemoLocation moimMemoLocation) {
        moimMemoLocationImgRepository.deleteMoimMemoLocationImgByMoimMemoLocation(moimMemoLocation);
    }

    public void removeMoimMemoLocationImgs(List<MoimMemoLocation> moimMemoLocations) {
        moimMemoLocationImgRepository.deleteMoimMemoLocationImgByMoimMemoLocation(moimMemoLocations);
    }

    public void removeMoimMemoLocation(MoimMemoLocation moimMemoLocation) {
        moimMemoLocationRepository.delete(moimMemoLocation);
    }

    public List<MoimMemoLocation> getMoimMemoLocationWithImgs(MoimMemo moimMemo) {
        return moimMemoLocationRepository.findMoimMemo(moimMemo);
    }

    public List<MoimMemoLocationImg> getMoimMemoLocationImgs(List<MoimMemoLocation> moimMemoLocations) {
        return moimMemoLocationImgRepository
                .findMoimMemoLocationImgsByMoimMemoLocations(moimMemoLocations);
    }

    public List<MoimMemoLocation> getMoimMemoLocations(MoimSchedule moimSchedule) {
        return moimMemoLocationRepository.findMoimMemoLocationsWithImgs(moimSchedule);
    }

    public List<MoimMemoLocationAndUser> getMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations) {
        return moimMemoLocationRepository.findMoimMemoLocationAndUsers(moimMemoLocations);
    }

}
