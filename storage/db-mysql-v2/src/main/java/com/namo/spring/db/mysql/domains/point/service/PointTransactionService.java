package com.namo.spring.db.mysql.domains.point.service;

import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.repository.PointTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointTransactionService {

    private final PointTransactionRepository repository;

    public void save(PointTransaction pointTransaction) {
        repository.save(pointTransaction);
    }

}
