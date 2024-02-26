package com.example.namo2.domain.memo.application.converter;

import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.memo.ui.dto.MoimMemoResponse;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoimMemoResponseConverter {
    private MoimMemoResponseConverter() {
        throw new IllegalStateException("Utill Classes");
    }

    public static MoimMemoResponse.MoimMemoDto toMoimMemoDto(
            MoimMemo moimMemo,
            List<MoimMemoLocation> moimMemoLocations,
            List<MoimMemoLocationAndUser> moimMemoLocationAndUsers) {
        MoimMemoResponse.MoimMemoDto moimMemoDtos = MoimMemoResponse.MoimMemoDto
                .builder()
                .moimMemo(moimMemo)
                .moimMemoLocationDtos(toMoimMemoLocationDtos(moimMemoLocations, moimMemoLocationAndUsers))
                .build();

        return moimMemoDtos;
    }

    private static List<MoimMemoResponse.MoimMemoLocationDto> toMoimMemoLocationDtos(
            List<MoimMemoLocation> moimMemoLocations,
            List<MoimMemoLocationAndUser> moimMemoLocationAndUsers) {
        Map<MoimMemoLocation, List<MoimMemoLocationAndUser>> moimMemoLocationMappingUsers = moimMemoLocationAndUsers
                .stream()
                .collect(Collectors.groupingBy(moimMemoLocationAndUser -> moimMemoLocationAndUser.getMoimMemoLocation()));

        List<MoimMemoResponse.MoimMemoLocationDto> moimMemoLocationDtos = moimMemoLocations.stream()
                .map(
                        moimMemoLocation -> MoimMemoResponse.MoimMemoLocationDto
                                .builder()
                                .moimMemoLocationId(moimMemoLocation.getId())
                                .name(moimMemoLocation.getName())
                                .money(moimMemoLocation.getTotalAmount())
                                .urls(moimMemoLocation.getMoimMemoLocationImgs()
                                        .stream()
                                        .map(MoimMemoLocationImg::getUrl)
                                        .collect(Collectors.toList()))
                                .participants(moimMemoLocationMappingUsers.get(moimMemoLocation))
                                .build()
                )
                .collect(Collectors.toList());
        return moimMemoLocationDtos;
    }

    public static MoimMemoResponse.SliceDiaryDto<MoimMemoResponse.DiaryDto> toSliceDiaryDto(
            List<MoimScheduleAndUser> moimScheduleAndUsers,
            Pageable page) {
        boolean hasNext = false;
        if (moimScheduleAndUsers.size() > page.getPageSize()) {
            moimScheduleAndUsers.remove(page.getPageSize());
            hasNext = true;
        }
        SliceImpl<MoimScheduleAndUser> moimSchedulesSlice = new SliceImpl<>(moimScheduleAndUsers, page, hasNext);
        Slice<MoimMemoResponse.DiaryDto> diarySlices = moimSchedulesSlice.map(MoimMemoResponse.DiaryDto::new);
        return new MoimMemoResponse.SliceDiaryDto(diarySlices);
    }
}
