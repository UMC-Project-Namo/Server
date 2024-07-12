package com.namo.spring.application.external.api.individual.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.individual.converter.DiaryResponseConverter;
import com.namo.spring.application.external.api.individual.converter.ImageConverter;
import com.namo.spring.application.external.api.individual.dto.DiaryResponse;
import com.namo.spring.application.external.api.individual.service.ImageService;
import com.namo.spring.application.external.api.individual.service.ScheduleService;
import com.namo.spring.application.external.api.user.service.UserService;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryFacade {

	private final UserService userService;
	private final ScheduleService scheduleService;
	private final ImageService imageService;
	private final FileUtils fileUtils;

	@Transactional
	public DiaryResponse.ScheduleIdDto createDiary(
		Long scheduleId,
		String content,
		List<MultipartFile> imgs
	) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.updateDiaryContents(content);
		if (imgs != null) {
			List<String> urls = fileUtils.uploadImages(imgs, FilePath.INVITATION_ACTIVITY_IMG);
			List<Image> imgList = urls.stream().map(url -> ImageConverter.toImage(url, schedule)).toList();
			imageService.createImages(imgList);
		}
		return DiaryResponseConverter.toScheduleIdRes(schedule);
	}

	public DiaryResponse.SliceDiaryDto getMonthDiary(
		Long userId,
		List<LocalDateTime> localDateTimes,
		Pageable pageable
	) {
		User user = userService.getUser(userId);
		return scheduleService.getScheduleDiaryByUser(user, localDateTimes.get(0), localDateTimes.get(1), pageable);
	}

	@Transactional(readOnly = true)
	public List<DiaryResponse.GetDiaryByUserDto> getAllDiariesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllDiariesByUser(user);
	}

	@Transactional(readOnly = true)
	public DiaryResponse.GetDiaryByScheduleDto getDiaryBySchedule(Long scheduleId) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.existDairy(); //다이어리 없으면 exception발생
		return DiaryResponseConverter.toGetDiaryByScheduleRes(schedule);
	}

	@Transactional
	public void removeDiary(Long scheduleId) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.deleteDiary();
		List<String> urls = schedule.getImages().stream()
			.map(Image::getImgUrl)
			.toList();
		imageService.removeImagesBySchedule(schedule);
		fileUtils.deleteImages(urls, FilePath.INVITATION_ACTIVITY_IMG);
	}

	@Transactional
	public void removeDiaryImage(Long scheduleId, Long imgId) {
		Image img = imageService.getImage(imgId);
		String imgUrl = img.getImgUrl();
		imageService.removeImage(scheduleId, img);
		fileUtils.delete(imgUrl, FilePath.INVITATION_ACTIVITY_IMG);
	}
}
