package com.namo.spring.application.external.api.point.converter;

import java.util.List;

import org.springframework.data.domain.Page;

import com.namo.spring.application.external.api.point.dto.PointResponse;
import com.namo.spring.db.mysql.domains.point.entity.Point;
import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
import com.namo.spring.db.mysql.domains.point.enums.TransactionType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

public class PointTransactionConverter {

    public static PointTransaction toChargeRequestTransaction(Point point, Long amount){
        return PointTransaction.builder()
                .point(point)
                .transactionType(TransactionType.CHARGE)
                .transactionStatus(TransactionStatus.PENDING)
                .amount(amount)
                .description(TransactionType.CHARGE.getDescription())
                .build();
    }

    public static PointResponse.ChargePointRequestListDto toChargePointRequestListDto(
            Page<PointTransaction> pointTransactions) {
        List<PointResponse.ChargePointRequestDto> chargePointRequests = pointTransactions
                .getContent()
                .stream()
                .map(PointTransactionConverter::toChargePointRequestDto)
                .toList();

        return PointResponse.ChargePointRequestListDto.builder()
                .chargePointRequests(chargePointRequests)
                .currentPage(pointTransactions.getNumber() + 1)
                .pageSize(pointTransactions.getSize())
                .totalItems(pointTransactions.getTotalElements())
                .totalPages(pointTransactions.getTotalPages())
                .build();
    }

    public static PointResponse.ChargePointRequestDto toChargePointRequestDto(PointTransaction pointTransaction){
        Member pointMember = pointTransaction.getPoint().getMember();
        return PointResponse.ChargePointRequestDto.builder()
                .profileImage(pointMember.getProfileImage())
                .nickname(pointMember.getNickname())
                .tag(pointMember.getTag())
                .pointTransactionId(pointTransaction.getId())
                .amount(pointTransaction.getAmount())
                .requestDate(pointTransaction.getTransactionDate())
                .build();
    }
}
