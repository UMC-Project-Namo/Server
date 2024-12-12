package com.namo.spring.application.external.api.point.converter;

import com.namo.spring.db.mysql.domains.point.entity.Point;
import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
import com.namo.spring.db.mysql.domains.point.enums.TransactionType;

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
}
