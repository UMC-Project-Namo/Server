package com.namo.spring.db.mysql.domains.point.event;

import jakarta.transaction.Transactional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.namo.spring.db.mysql.domains.point.entity.Point;
import com.namo.spring.db.mysql.domains.point.repository.PointRepository;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.event.MemberCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointEventListener {
    private final PointRepository pointRepository;

    @EventListener
    @Transactional
    public void handleMemberCreated(MemberCreatedEvent event) {
        Member member = event.getMember();
        Point point = Point.createInitialPoint(member);
        pointRepository.save(point);
    }
}
