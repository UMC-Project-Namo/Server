package com.namo.spring.db.mysql.domains.record.entity;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ActivityParticipant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(nullable = false)
    private BigDecimal amount;

    private boolean isIncludedInSettlement;

    @Builder
    public ActivityParticipant(Participant participant, Activity activity) {
        this.participant = Objects.requireNonNull(participant, "participant는 null일 수 없습니다.");
        this.activity = Objects.requireNonNull(activity, "activity은 null일 수 없습니다.");
        this.amount = BigDecimal.ZERO;
        this.isIncludedInSettlement = false;
    }

    public void setSettlementInfo(BigDecimal amountPerPerson) {
        this.amount = amountPerPerson;
        this.isIncludedInSettlement = true;
    }

    public void departSettlement(){
        this.amount = BigDecimal.ZERO;
        this.isIncludedInSettlement = false;
    }
}
