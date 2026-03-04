package com.example.repository;

import com.example.entity.TransactionEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
@Repository
public interface TransactionalRepository extends JpaRepository<TransactionEntity,Long> {
    List<TransactionEntity> findByAccountNumber(String accountNumber);
}
