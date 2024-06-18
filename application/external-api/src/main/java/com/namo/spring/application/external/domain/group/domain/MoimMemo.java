package com.namo.spring.application.external.domain.group.domain;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moim_memo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimMemo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_memo_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_schedule_id")
    private MoimSchedule moimSchedule;

    @OneToMany(mappedBy = "moimMemo", fetch = FetchType.LAZY)
    private List<MoimMemoLocation> moimMemoLocations = new ArrayList<>();

    @Builder
    public MoimMemo(Long id, MoimSchedule moimSchedule) {
        this.id = id;
        this.moimSchedule = moimSchedule;
        this.moimSchedule.registerMemo(this);
    }

    public boolean isLastMoimMemoLocations() {
        return moimMemoLocations != null && moimMemoLocations.size() == 1;
    }

    public boolean isFullLocationSize() {
        if (moimMemoLocations.size() >= 3) {
            return true;
        }
        return false;
    }
}
