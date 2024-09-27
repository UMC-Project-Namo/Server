package com.namo.spring.application.external.api.record.serivce;

import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.record.converter.ActivityParticipantConverter;
import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.record.exception.ActivityParticipantException;
import com.namo.spring.db.mysql.domains.record.service.ActivityUserService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityParticipantManageService {

    private final ActivityUserService activityUserService;
    private final ParticipantManageService participantManageService;

    /**
     * 활동 참여자를 생성하는 메서드입니다. (스케줄의 참여자 정보를 찾아 활동 참여 정보를 생성합니다)
     * !! 중복 참여자 여부, 참여자의 스케줄 참여 여부를 검증합니다.
     *
     * @param activity
     * @param participantIdList
     * @param scheduleId
     * @return
     */
    public List<ActivityParticipant> createActivityParticipant(Activity activity, List<Long> participantIdList,
            Long scheduleId) {
        validateDuplicateParticipantIds(participantIdList);
        List<Participant> participants = participantManageService.getParticipantsInSchedule(participantIdList,
                scheduleId);
        List<ActivityParticipant> activityParticipants = participants.stream()
                .map(participant -> ActivityParticipantConverter.toActivityParticipant(participant, activity))
                .toList();

        return activityUserService.createActivityParticipants(activityParticipants);
    }

    /**
     * 정산 정보를 추가하는 메서드입니다.
     * 활동 참여자들 중 정산에 참여하는 사람에게 정산 참여 여부를 갱신하고 인당 금액을 추가합니다.
     * @param settlement
     * @param activityParticipants
     */
    public void addSettlement(ActivityRequest.ActivitySettlementDto settlement, List<ActivityParticipant> activityParticipants) {
        activityParticipants.stream()
                .filter(activityParticipant ->
                        settlement.getParticipantIdList().contains(activityParticipant.getParticipant().getId()))
                .forEach(activityParticipant -> {
                    activityParticipant.setSettlementInfo(settlement.getAmountPerPerson());
                });
    }

    /**
     * 활동 참여자를 업데이트하는 메서드입니다.
     * 추가할 참여자에대해 활동 참여 정보를 만들고, 삭제할 참여자는 참여정보가 삭제됩니다.
     * !! 활동 삭제시 정산 미참여자만 삭제가 가능합니다.
     * @param activity
     * @param request
     */
    public void updateActivityParticipants(Activity activity, ActivityRequest.UpdateActivityParticipantsDto request) {
        List<ActivityParticipant> deleteTarget = activityUserService.readAllByActivityAndParticipantIdAndSettlementStatus(activity,
                request.getDeleteParticipantIdList(), false);
        if ((long)deleteTarget.size() != request.getDeleteParticipantIdList().size()){
            throw new ActivityParticipantException(ErrorStatus.IN_SETTLEMENT_ACTIVITY_MEMBER);
        }
        activityUserService.deleteAll(deleteTarget);
        createActivityParticipant(activity, request.getAddParticipantIdList(), activity.getSchedule().getId());
    }
}
