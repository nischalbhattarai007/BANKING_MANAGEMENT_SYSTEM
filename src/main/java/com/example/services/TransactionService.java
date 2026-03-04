package com.example.services;

import com.example.entity.AccountEntity;
import com.example.entity.TransactionEntity;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionalRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Singleton
public class TransactionService {
    private final TransactionalRepository transactionalRepository;
    private final AccountRepository accountRepository;
    @Inject
    public TransactionService(TransactionalRepository transactionalRepository, AccountRepository accountRepository) {
        this.transactionalRepository = transactionalRepository;
        this.accountRepository = accountRepository;
    }
    @Transactional
    public TransactionEntity deposit(String accountNumber,double amount){
        if(amount<=0){
            throw new RuntimeException("Deposit amount cannot be less than zero");
        }
        AccountEntity accountEntity=accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        accountEntity.setBalance(accountEntity.getBalance() + amount);
        accountRepository.save(accountEntity);
        TransactionEntity txn=new TransactionEntity();
        txn.setAccountNumber(accountNumber);
        txn.setAmount(amount);
        txn.setTransactionType("DEPOSIT");
        txn.setCreatedAt(LocalDateTime.now());
        log.info("Deposited {} to account :{}",amount,accountNumber);
        return transactionalRepository.save(txn);
    }
    @Transactional
    public TransactionEntity withdraw(String accountNumber, double amount){
        if(amount<=0){
            throw new RuntimeException("Please enter an amount to proceed");
        }
        AccountEntity accountEntity=accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        if(accountEntity.getBalance()<amount){
            throw new RuntimeException("Insufficient Amount ! Your balance is only : " + accountEntity.getBalance());
        }
        accountEntity.setBalance(accountEntity.getBalance()-amount);
        accountRepository.save(accountEntity);
        TransactionEntity txn=new TransactionEntity();
        txn.setAccountNumber(accountNumber);
        txn.setAmount(amount);
        txn.setTransactionType("WITHDRAW");
        txn.setCreatedAt(LocalDateTime.now());
        log.info("Withdrawn {} from account :{}",amount,accountNumber);
        return transactionalRepository.save(txn);
    }
    @Transactional
    public void transfer(String fromAccountNumber,String toAccountNumber,double amount){
        if(amount<=0){
            throw new RuntimeException("Please enter an amount to send");
        }
        AccountEntity sender=accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(()-> new RuntimeException("Sender Account not found"));
        AccountEntity receiver=accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(()-> new RuntimeException("Receiver Account not found"));
        fromAccountNumber=fromAccountNumber.trim();
        toAccountNumber=toAccountNumber.trim();
        if(fromAccountNumber.equals(toAccountNumber)){
            throw new RuntimeException("Account number cannot be same");
        }

        if(sender.getBalance()<amount){
            throw new RuntimeException("Insufficient Amount ! Your balance is only : "  +sender.getBalance());
        }
        sender.setBalance(sender.getBalance()-amount);
        receiver.setBalance(receiver.getBalance() + amount);
        accountRepository.save(sender);
        accountRepository.save(receiver);
    }
}
