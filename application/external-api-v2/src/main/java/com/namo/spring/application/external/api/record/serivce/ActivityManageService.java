package com.namo.spring.application.external.api.record.serivce;

import org.springframework.stereotype.Component;

import com.namo.spring.application.external.api.record.converter.ActivityConverter;
import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.application.external.api.record.serivce.image.ActivityImageManageService;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.exception.ActivityException;
import com.namo.spring.db.mysql.domains.record.service.ActivityService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityManageService {

    private final ActivityService activityService;
    private final ParticipantManageService participantManageService;
    private final ActivityImageManageService activityImageManageService;

    /**
     * 활동을 찾는 메서드입니다. (스케줄 참여자만 활동들의 정산내역을 확인할 수 있습니다)
     * !! 활동이 존재 하는지, 참여한 스케줄 인지 검증합니다.
     *
     * @param memberId
     * @param activityId
     * @return
     */
    public Activity getMyActivity(Long memberId, Long activityId) {
        Activity activity = activityService.readActivity(activityId)
                .orElseThrow(() -> new ActivityException(ErrorStatus.NOT_FOUND_GROUP_ACTIVITY_FAILURE));

        boolean isParticipant = activity.getSchedule().getParticipantList().stream()
                .anyMatch(participant -> participant.getMember().getId().equals(memberId));
        if (!isParticipant) {
            throw new ActivityException(ErrorStatus.NOT_PARTICIPATING_ACTIVITY);
        }
        return activity;
    }

    /**
     * 활동을 생성 하는 메서드입니다.
     * 활동, 활동 이미지를 생성합니다.
     * !! 활동 참여 정보를 검증합니다.
     * @param memberId
     * @param scheduleId
     * @param request
     * @return
     */
    public Activity createActivity(Long memberId, Long scheduleId, ActivityRequest.CreateActivityDto request) {
        Participant myParticipant = participantManageService.getParticipantByMemberAndSchedule(memberId, scheduleId);
        Activity activity = activityService.createActivity(ActivityConverter.toActivity(myParticipant.getSchedule(), request));
        // 활동 이미지 생성
        if (request.getImageList() != null && !request.getImageList().isEmpty()){
            activityImageManageService.createImages(activity, request.getImageList());
        }
        return activity;
    }

    public void removeActivity(Activity activity) {
        activity.getActivityImages().forEach(activityImage ->
                activityImageManageService.deleteFromCloud(activityImage.getId()));
        activityImageManageService.deleteImages(activity);
        activityService.deleteActivity(activity);
    }
}
