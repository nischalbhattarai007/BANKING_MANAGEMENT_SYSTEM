package com.example.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Serdeable
@Table(name="transaction_details")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @NotBlank(message = "Account number must not be empty")
    @Column(nullable = false)
    private String accountNumber;

    @Positive(message = "Amount must be greater than zero")
    @Column(nullable = false)
    private double amount;

    @NotBlank(message = "Transaction type must not be empty")
    @Column(nullable = false)
    private String transactionType;
    // Values: DEPOSIT or WITHDRAW

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
