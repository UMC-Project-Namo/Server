package com.namo.spring.application.external.api.group.converter;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import com.namo.spring.application.external.api.group.dto.GroupDiaryResponse;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocation;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationImg;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;

public class MoimDiaryResponseConverter {
	private MoimDiaryResponseConverter() {
		throw new IllegalStateException("Utill Classes");
	}

	public static GroupDiaryResponse.MoimDiaryDto toMoimDiaryDto(
		MoimMemo moimMemo,
		List<MoimMemoLocation> moimMemoLocations,
		List<MoimMemoLocationAndUser> moimMemoLocationAndUsers) {
		Long startDate = moimMemo.getMoimSchedule().getPeriod().getStartDate()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.getEpochSecond();
		List<GroupDiaryResponse.GroupUserDto> users = moimMemo.getMoimSchedule().getMoimScheduleAndUsers().stream()
			.map(MoimDiaryResponseConverter::toGroupUserDto)
			.toList();
		return GroupDiaryResponse.MoimDiaryDto
			.builder()
			.name(moimMemo.getMoimSchedule().getName())
			.startDate(startDate)
			.locationName(moimMemo.getMoimSchedule().getLocation().getLocationName())
			.users(users)
			.moimActivityDtos(toMoimActivityDtos(moimMemoLocations, moimMemoLocationAndUsers))
			.build();
	}

	public static GroupDiaryResponse.GroupUserDto toGroupUserDto(MoimScheduleAndUser moimScheduleAndUser) {
		return GroupDiaryResponse.GroupUserDto
			.builder()
			.userId(moimScheduleAndUser.getUser().getId())
			.userName(moimScheduleAndUser.getUser().getName())
			.build();
	}

	private static List<GroupDiaryResponse.MoimActivityDto> toMoimActivityDtos(
		List<MoimMemoLocation> moimMemoLocations,
		List<MoimMemoLocationAndUser> moimMemoLocationAndUsers) {
		Map<MoimMemoLocation, List<MoimMemoLocationAndUser>> moimActivityMappingUsers = moimMemoLocationAndUsers
			.stream()
			.collect(Collectors.groupingBy(MoimMemoLocationAndUser::getMoimMemoLocation));

		return moimMemoLocations.stream()
			.map(moimMemoLocation -> toMoimActivityDto(moimActivityMappingUsers, moimMemoLocation))
			.collect(Collectors.toList());
	}

	private static GroupDiaryResponse.MoimActivityDto toMoimActivityDto(
		Map<MoimMemoLocation, List<MoimMemoLocationAndUser>> moimMemoLocationMappingUsers,
		MoimMemoLocation moimMemoLocation) {
		List<String> urls = moimMemoLocation.getMoimMemoLocationImgs().stream()
			.map(MoimMemoLocationImg::getUrl)
			.toList();
		List<Long> participants = moimMemoLocationMappingUsers.get(moimMemoLocation).stream()
			.map(moimMemoLocationAndUser -> moimMemoLocationAndUser.getUser().getId())
			.toList();
		return GroupDiaryResponse.MoimActivityDto
			.builder()
			.moimActivityId(moimMemoLocation.getId())
			.name(moimMemoLocation.getName())
			.money(moimMemoLocation.getTotalAmount())
			.urls(urls)
			.participants(participants)
			.build();
	}

	public static GroupDiaryResponse.SliceDiaryDto toSliceDiaryDto(
		List<MoimScheduleAndUser> moimScheduleAndUsers,
		Pageable page
	) {
		boolean hasNext = false;
		if (moimScheduleAndUsers.size() > page.getPageSize()) {
			moimScheduleAndUsers.remove(page.getPageSize());
			hasNext = true;
		}
		SliceImpl<MoimScheduleAndUser> moimSchedulesSlice = new SliceImpl<>(moimScheduleAndUsers, page, hasNext);
		return GroupDiaryResponse.SliceDiaryDto.builder()
			.content(
				moimSchedulesSlice.stream().map(MoimDiaryResponseConverter::toDiaryDto).collect(Collectors.toList()))
			.currentPage(moimSchedulesSlice.getNumber())
			.size(moimSchedulesSlice.getSize())
			.first(moimSchedulesSlice.isFirst())
			.last(moimSchedulesSlice.isLast())
			.build();
	}

	public static GroupDiaryResponse.DiaryDto toDiaryDto(Schedule schedule) {
		Long startDate = schedule.getPeriod().getStartDate()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.getEpochSecond();
		List<String> urls = schedule.getImages().stream()
			.map(Image::getImgUrl)
			.toList();
		return GroupDiaryResponse.DiaryDto.builder()
			.scheduleId(schedule.getId())
			.name(schedule.getName())
			.startDate(startDate)
			.contents(schedule.getContents())
			.urls(urls)
			.categoryId(schedule.getCategory().getId())
			.color(schedule.getCategory().getPalette().getId())
			.placeName(schedule.getLocation().getLocationName())
			.build();
	}

	public static GroupDiaryResponse.DiaryDto toDiaryDto(MoimScheduleAndUser moimScheduleAndUser) {
		Long startDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getStartDate()
			.atZone(ZoneId.systemDefault())
			.toInstant()
			.getEpochSecond();
		List<String> urls = moimScheduleAndUser.getMoimSchedule().getMoimMemo()
			.getMoimMemoLocations()
			.stream()
			.flatMap(location -> location
				.getMoimMemoLocationImgs()
				.stream())
			.map(MoimMemoLocationImg::getUrl)
			.limit(3)
			.collect(Collectors.toList());
		return GroupDiaryResponse.DiaryDto.builder()
			.scheduleId(moimScheduleAndUser.getMoimSchedule().getId())
			.name(moimScheduleAndUser.getMoimSchedule().getName())
			.startDate(startDate)
			.contents(moimScheduleAndUser.getMemo())
			.urls(urls)
			.categoryId(moimScheduleAndUser.getCategory().getId())
			.color(moimScheduleAndUser.getCategory().getPalette().getId())
			.placeName(moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName())
			.build();
	}
}
