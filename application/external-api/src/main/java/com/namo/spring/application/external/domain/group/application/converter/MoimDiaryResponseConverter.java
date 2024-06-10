package com.example.namo2.domain.group.application.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.example.namo2.domain.group.domain.MoimMemo;
import com.example.namo2.domain.group.domain.MoimMemoLocation;
import com.example.namo2.domain.group.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.group.domain.MoimMemoLocationImg;
import com.example.namo2.domain.group.domain.MoimScheduleAndUser;
import com.example.namo2.domain.group.ui.dto.GroupDiaryResponse;

public class MoimDiaryResponseConverter {
	private MoimDiaryResponseConverter() {
		throw new IllegalStateException("Utill Classes");
	}

	public static GroupDiaryResponse.MoimDiaryDto toMoimMemoDto(
		MoimMemo moimMemo,
		List<MoimMemoLocation> moimMemoLocations,
		List<MoimMemoLocationAndUser> moimMemoLocationAndUsers) {
		return GroupDiaryResponse.MoimDiaryDto
			.builder()
			.moimMemo(moimMemo)
			.moimActivityDtos(toMoimActivityDtos(moimMemoLocations, moimMemoLocationAndUsers))
			.build();
	}

	private static List<GroupDiaryResponse.MoimActivityDto> toMoimActivityDtos(
		List<MoimMemoLocation> moimMemoLocations,
		List<MoimMemoLocationAndUser> moimMemoLocationAndUsers) {
		Map<MoimMemoLocation, List<MoimMemoLocationAndUser>> moimActivityMappingUsers = moimMemoLocationAndUsers
			.stream()
			.collect(Collectors.groupingBy(MoimMemoLocationAndUser::getMoimMemoLocation));

		return moimMemoLocations.stream()
			.map(moimMemoLocation -> toMoimActivateDto(moimActivityMappingUsers, moimMemoLocation))
			.collect(Collectors.toList());
	}

	private static GroupDiaryResponse.MoimActivityDto toMoimActivateDto(
		Map<MoimMemoLocation, List<MoimMemoLocationAndUser>> moimMemoLocationMappingUsers,
		MoimMemoLocation moimMemoLocation) {
		return GroupDiaryResponse.MoimActivityDto
			.builder()
			.moimActivityId(moimMemoLocation.getId())
			.name(moimMemoLocation.getName())
			.money(moimMemoLocation.getTotalAmount())
			.urls(moimMemoLocation.getMoimMemoLocationImgs().stream()
				.map(MoimMemoLocationImg::getUrl)
				.toList())
			.participants(moimMemoLocationMappingUsers.get(moimMemoLocation))
			.build();
	}

	public static GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto> toSliceDiaryDto(
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		Pageable page
	) {
		boolean hasNext = false;
		if (moimScheduleAndUsers.size() > page.getPageSize()) {
			moimScheduleAndUsers.remove(page.getPageSize());
			hasNext = true;
		}
		SliceImpl<MoimScheduleAndUser> moimSchedulesSlice = new SliceImpl<>(moimScheduleAndUsers, page, hasNext);
		Slice<GroupDiaryResponse.DiaryDto> diarySlices = moimSchedulesSlice.map(GroupDiaryResponse.DiaryDto::new);
		return new GroupDiaryResponse.SliceDiaryDto<>(diarySlices);
	}

	public static GroupDiaryResponse.DiaryDto toDiaryDto(MoimScheduleAndUser moimScheduleAndUser) {
		return new GroupDiaryResponse.DiaryDto(moimScheduleAndUser);
	}
}
