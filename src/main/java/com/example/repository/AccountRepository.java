package com.example.repository;

import com.example.entity.AccountEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
