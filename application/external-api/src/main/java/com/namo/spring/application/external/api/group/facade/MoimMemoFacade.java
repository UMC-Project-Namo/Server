package com.namo.spring.application.external.api.group.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.group.converter.MoimDiaryResponseConverter;
import com.namo.spring.application.external.api.group.converter.MoimMemoConverter;
import com.namo.spring.application.external.api.group.converter.MoimMemoLocationConverter;
import com.namo.spring.application.external.api.group.dto.GroupDiaryRequest;
import com.namo.spring.application.external.api.group.dto.GroupDiaryResponse;
import com.namo.spring.application.external.api.group.dto.GroupScheduleRequest;
import com.namo.spring.application.external.api.group.service.GroupActivityService;
import com.namo.spring.application.external.api.group.service.GroupMemoService;
import com.namo.spring.application.external.api.group.service.GroupScheduleAndUserService;
import com.namo.spring.application.external.api.group.service.GroupScheduleService;
import com.namo.spring.application.external.api.user.service.UserService;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocation;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationImg;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoimMemoFacade {
	private final GroupScheduleService moimScheduleService;

	private final GroupScheduleAndUserService moimScheduleAndUserService;
	private final GroupMemoService moimMemoService;
	private final GroupActivityService moimMemoLocationService;
	private final UserService userService;

	private final FileUtils fileUtils;

	@Transactional(readOnly = false)
	public void createMoimMemo(Long moimScheduleId, GroupDiaryRequest.LocationDto locationDto,
		List<MultipartFile> imgs) {
		MoimMemo moimMemo = getMoimMemo(moimScheduleId);
		MoimMemoLocation moimMemoLocation = createMoimMemoLocation(moimMemo, locationDto);

		createMoimMemoLocationAndUsers(locationDto, moimMemoLocation);
		createMoimMemoLocationImgs(imgs, moimMemoLocation);
	}

	private MoimMemo getMoimMemo(Long moimScheduleId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
		return moimMemoService.getMoimMemoOrNull(moimSchedule)
			.orElseGet(
				() -> moimMemoService.createMoimMemo(MoimMemoConverter.toMoimMemo(moimSchedule))
			);
	}

	private MoimMemoLocation createMoimMemoLocation(MoimMemo moimMemo, GroupDiaryRequest.LocationDto locationDto) {
		MoimMemoLocation moimMemoLocation = MoimMemoLocationConverter.toMoimMemoLocation(moimMemo, locationDto);
		return moimMemoLocationService.createMoimMemoLocation(moimMemoLocation, moimMemo);
	}

	private void createMoimMemoLocationAndUsers(GroupDiaryRequest.LocationDto locationDto,
		MoimMemoLocation moimMemoLocation) {
		List<User> users = userService.getUsersInMoimSchedule(locationDto.getParticipants());
		List<MoimMemoLocationAndUser> moimMemoLocationAndUsers = MoimMemoLocationConverter
			.toMoimMemoLocationLocationAndUsers(moimMemoLocation, users);
		moimMemoLocationService.createMoimMemoLocationAndUsers(moimMemoLocationAndUsers);
	}

	/**
	 * TODO: 적절한 validation: 처리 필요
	 */
	private void createMoimMemoLocationImgs(List<MultipartFile> imgs, MoimMemoLocation moimMemoLocation) {
		if (imgs == null) {
			return;
		}
		/**
		 * imgs 에 대한 validation 처리 필요
		 * 값이 3개 이상일 경우 OVER_IMAGES_FAILURE 필요
		 */
		List<String> urls = fileUtils.uploadImages(imgs, FilePath.GROUP_ACTIVITY_IMG);
		for (String url : urls) {
			MoimMemoLocationImg moimMemoLocationImg = MoimMemoLocationConverter
				.toMoimMemoLocationLocationImg(moimMemoLocation, url);
			moimMemoLocationService.createMoimMemoLocationImg(moimMemoLocationImg);
		}
	}

	@Transactional(readOnly = false)
	public void modifyMoimMemoLocation(Long memoLocationId, GroupDiaryRequest.LocationDto locationDto,
		List<MultipartFile> imgs) {
		MoimMemoLocation moimMemoLocation = moimMemoLocationService.getMoimMemoLocationWithImgs(memoLocationId);
		moimMemoLocation.update(locationDto.getName(), locationDto.getMoney());

		moimMemoLocationService.removeMoimMemoLocationAndUsers(moimMemoLocation);
		createMoimMemoLocationAndUsers(locationDto, moimMemoLocation);

		removeMoimMemoLocationImgs(moimMemoLocation);
		createMoimMemoLocationImgs(imgs, moimMemoLocation);
	}

	private void removeMoimMemoLocationImgs(MoimMemoLocation moimMemoLocation) {
		List<String> urls = moimMemoLocation.getMoimMemoLocationImgs()
			.stream()
			.map(MoimMemoLocationImg::getUrl)
			.toList();
		fileUtils.deleteImages(urls, FilePath.GROUP_ACTIVITY_IMG);
		moimMemoLocationService.removeMoimMemoLocationImgs(moimMemoLocation);
	}

	@Transactional(readOnly = false)
	public void removeMoimMemoLocation(Long memoLocationId) {
		MoimMemoLocation moimMemoLocation = moimMemoLocationService.getMoimMemoLocationWithImgs(memoLocationId);

		moimMemoLocationService.removeMoimMemoLocationAndUsers(moimMemoLocation);
		removeMoimMemoLocationImgs(moimMemoLocation);
		moimMemoLocationService.removeMoimMemoLocation(moimMemoLocation);
	}

	@Transactional(readOnly = false)
	public GroupDiaryResponse.GroupDiaryDto getMoimMemoWithLocations(Long moimScheduleId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
		MoimMemo moimMemo = moimMemoService.getMoimMemoWithUsers(moimSchedule);
		List<MoimMemoLocation> moimMemoLocations = moimMemoLocationService.getMoimMemoLocations(moimSchedule);
		List<MoimMemoLocationAndUser> moimMemoLocationAndUsers
			= moimMemoLocationService.getMoimMemoLocationAndUsers(moimMemoLocations);
		return MoimDiaryResponseConverter.toGroupDiaryDto(moimMemo, moimMemoLocations, moimMemoLocationAndUsers);
	}

	@Transactional(readOnly = true)
	public GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto> getMonthMonthMoimMemo(Long userId,
		List<LocalDateTime> dates, Pageable page) {
		User user = userService.getUser(userId);
		List<MoimScheduleAndUser> moimScheduleAndUsersForMonthMoimMemo
			= moimScheduleAndUserService.getMoimScheduleAndUsersForMonthMoimMemo(user, dates, page);
		return MoimDiaryResponseConverter.toSliceDiaryDto(moimScheduleAndUsersForMonthMoimMemo, page);
	}

	@Transactional(readOnly = false)
	public void createMoimScheduleText(Long moimScheduleId, Long userId,
		GroupScheduleRequest.PostGroupScheduleTextDto moimScheduleText) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
		User user = userService.getUser(userId);
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);
		moimScheduleAndUserService.modifyText(moimScheduleAndUser, moimScheduleText.getText());
	}

	@Transactional(readOnly = false)
	public void removeMoimMemo(Long moimScheduleId) {
		MoimMemo moimMemoWithLocations = moimMemoService.getMoimMemoWithLocations(moimScheduleId);
		for (MoimMemoLocation moimMemoLocation : moimMemoWithLocations.getMoimMemoLocations()) {
			removeMoimMemoLocation(moimMemoLocation.getId());
		}
		moimMemoService.removeMoimMemo(moimMemoWithLocations);
	}

	@Transactional(readOnly = false)
	public void removePersonMoimMemo(Long scheduleId, Long userId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(scheduleId);
		User user = userService.getUser(userId);
		MoimScheduleAndUser moimScheduleAndUser
			= moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);
		moimScheduleAndUserService.removeMoimScheduleMemoInPersonalSpace(moimScheduleAndUser);
	}

	@Transactional(readOnly = true)
	public GroupDiaryResponse.DiaryDto getMoimDiaryDetail(Long moimScheduleId, Long userId) {
		User user = userService.getUser(userId);
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimScheduleId,
			user);
		return MoimDiaryResponseConverter.toDiaryDto(moimScheduleAndUser);
	}
}
