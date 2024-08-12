package com.namo.spring.db.mysql.domains.record.entity;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Builder
    public ActivityParticipant(Participant participant, Activity activity, BigDecimal amount) {
        this.participant = Objects.requireNonNull(participant, "participant는 null일 수 없습니다.");
        this.activity = Objects.requireNonNull(activity, "activity은 null일 수 없습니다.");
        this.amount = Objects.requireNonNull(amount, "amount은 null일 수 없습니다.");
    }
}
