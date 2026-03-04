package com.example.services;

import com.example.entity.AccountEntity;
import com.example.exception.AccountNotFoundException;
import com.example.repository.AccountRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;
@Slf4j
@Singleton
public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    //create account
    public AccountEntity createAccount(AccountEntity accountEntity){
        if(accountEntity.getAccount_Holder_Name()==null || accountEntity.getAccount_Holder_Name().isEmpty()){
            throw new HttpStatusException(HttpStatus.BAD_REQUEST,"Account holder name is required");
        }
        if(accountEntity.getAccount_Type()==null || accountEntity.getAccount_Type().isEmpty()){
            throw new IllegalArgumentException("Account type is required");
        }
        accountEntity.setAccount_Holder_Name(accountEntity.getAccount_Holder_Name());
        accountEntity.setAccount_Type(accountEntity.getAccount_Type());
        accountEntity.setBalance(0.0);
        if(accountEntity.getAccountNumber()==null){
            accountEntity.setAccountNumber("ACC-" + UUID.randomUUID().toString().substring(0,8));
        }
        log.info("Account created for {} with account number {}" ,
                accountEntity.getAccount_Holder_Name(), accountEntity.getAccountNumber());
        return accountRepository.save(accountEntity);
    }
    public AccountEntity getAccountByNumber(String accountNumber){
            return accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException(" Account with number "
                            + accountNumber + "not found"));
    }
    public AccountEntity getAccountById(Long id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id  + "not found"  ));}
    public List<AccountEntity> getAllAccount(){
        return accountRepository.findAll();
    }

    }