package com.namo.spring.application.external.api.record.usecase;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.record.converter.ActivityResponseConverter;
import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.application.external.api.record.dto.ActivityResponse;
import com.namo.spring.application.external.api.record.serivce.ActivityManageService;
import com.namo.spring.application.external.api.record.serivce.ActivityParticipantManageService;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityUseCase {

    private final ParticipantManageService participantManageService;
    private final ActivityManageService activityManageService;
    private final ActivityParticipantManageService activityParticipantManageService;

    @Cacheable(value = "ActivityInfoDtoList", key = "#scheduleId", cacheManager = "redisCacheManager", unless = "#result==null")
    @Transactional(readOnly = true)
    public ActivityResponse.ActivityInfoDtoList getActivities(Long memberId, Long scheduleId) {
        Schedule schedule = participantManageService.getParticipantByMemberAndSchedule(memberId, scheduleId).getSchedule();
        List<Activity> activities = schedule.getActivityList();
        return ActivityResponseConverter.toActivityInfoDtoList(activities);
    }

    @Transactional(readOnly = true)
    public ActivityResponse.ActivitySettlementInfoDto getSettlement(Long memberId, Long activityId) {
        Activity activity = activityManageService.getMyActivity(memberId, activityId);
        return ActivityResponseConverter.toActivitySettlementInfoDto(activity);
    }

    @Transactional
    public void createActivity(Long memberId, Long scheduleId, ActivityRequest.CreateActivityDto request) {
        // 활동 생성
        Activity activity = activityManageService.createActivity(memberId, scheduleId, request);
        // 활동 참여 정보 생성
        List<ActivityParticipant> activityParticipant = activityParticipantManageService.createActivityParticipant(
                activity, request.getParticipantIdList(), scheduleId);
        // 정산 정보 생성
        if (request.getSettlement() != null && !request.getSettlement().getParticipantIdList().isEmpty()) {
            activity.setSettlementInfo(request.getSettlement().getTotalAmount());
            activityParticipantManageService.addSettlement(request.getSettlement(), activityParticipant);
        }
    }

    @Transactional
    public void updateActivity(Long memberId, Long activityId, ActivityRequest.UpdateActivityDto request) {
        Activity target = activityManageService.getMyActivity(memberId, activityId);
        activityManageService.updateActivity(target, request);
    }

    @Transactional
    public void updateActivityParticipants(Long memberId, Long activityId, ActivityRequest.UpdateActivityParticipantsDto request) {
        Activity target = activityManageService.getMyActivity(memberId, activityId);
        activityParticipantManageService.updateActivityParticipants(target, request);
    }

    @Transactional
    public void updateActivityTag(Long memberId, Long activityId, String tag) {
        Activity target = activityManageService.getMyActivity(memberId, activityId);
        target.updateTag(tag);
    }

    @Transactional
    public void updateActivitySettlement(Long memberId, Long activityId,
            ActivityRequest.UpdateActivitySettlementDto request) {
        Activity target = activityManageService.getMyActivity(memberId, activityId);
        target.setSettlementInfo(request.getTotalAmount());
        activityParticipantManageService.updateActivityParticipantsSettlement(target, request);

    }

    @Transactional
    public void deleteActivity(Long memberId, Long activityId) {
        Activity target = activityManageService.getMyActivity(memberId, activityId);
        activityManageService.removeActivity(target);
    }
}
