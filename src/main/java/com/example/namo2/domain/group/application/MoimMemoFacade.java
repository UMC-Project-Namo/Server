package com.example.namo2.domain.group.application;

import com.example.namo2.domain.group.application.converter.MoimDiaryResponseConverter;
import com.example.namo2.domain.group.application.converter.MoimMemoConverter;
import com.example.namo2.domain.group.application.converter.MoimMemoLocationConverter;
import com.example.namo2.domain.group.application.impl.MoimMemoLocationService;
import com.example.namo2.domain.group.application.impl.MoimMemoService;
import com.example.namo2.domain.group.application.impl.MoimScheduleAndUserService;
import com.example.namo2.domain.group.application.impl.MoimScheduleService;
import com.example.namo2.domain.group.domain.*;
import com.example.namo2.domain.group.ui.dto.GroupDiaryRequest;
import com.example.namo2.domain.group.ui.dto.GroupDiaryResponse;
import com.example.namo2.domain.group.ui.dto.GroupScheduleRequest;
import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.constant.FilePath;
import com.example.namo2.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoimMemoFacade {
    private final MoimScheduleService moimScheduleService;

    private final MoimScheduleAndUserService moimScheduleAndUserService;
    private final MoimMemoService moimMemoService;
    private final MoimMemoLocationService moimMemoLocationService;
    private final UserService userService;

    private final FileUtils fileUtils;

    @Transactional(readOnly = false)
    public void create(Long moimScheduleId, GroupDiaryRequest.LocationDto locationDto, List<MultipartFile> imgs) {
        MoimMemo moimMemo = getMoimMemo(moimScheduleId);
        MoimMemoLocation moimMemoLocation = createMoimMemoLocation(moimMemo, locationDto);

        createMoimMemoLocationAndUsers(locationDto, moimMemoLocation);
        createMoimMemoLocationImgs(imgs, moimMemoLocation);
    }

    private MoimMemo getMoimMemo(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
        return moimMemoService.getMoimMemoOrNull(moimSchedule)
                .orElseGet(
                        () -> moimMemoService.create(MoimMemoConverter.toMoimMemo(moimSchedule))
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
                .collect(Collectors.toList());
        fileUtils.deleteImages(urls);
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
    public GroupDiaryResponse.MoimDiaryDto getMoimMemoWithLocations(Long moimScheduleId) {
        MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleId);
        MoimMemo moimMemo = moimMemoService.getMoimMemoWithUsers(moimSchedule);
        List<MoimMemoLocation> moimMemoLocations = moimMemoLocationService.getMoimMemoLocations(moimSchedule);
        List<MoimMemoLocationAndUser> moimMemoLocationAndUsers
                = moimMemoLocationService.getMoimMemoLocationAndUsers(moimMemoLocations);
        return MoimDiaryResponseConverter.toMoimMemoDto(moimMemo, moimMemoLocations, moimMemoLocationAndUsers);
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
}
