package com.example.endpoint;

import com.example.*;
import com.example.entity.AccountEntity;
import com.example.services.AccountService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;
@GrpcService
public class AccountServiceEndPoint extends AccountServiceGrpc.AccountServiceImplBase {
    private final AccountService accountService;
    @Inject
    public AccountServiceEndPoint(AccountService accountService) {
        this.accountService = accountService;
    }
    //create account
    public void createAccount(AccountRequest accountRequest, StreamObserver<AccountResponse> responseObserver){
        AccountEntity accountEntity=new AccountEntity();
        //set account entity
        accountEntity.setAccount_Holder_Name(accountRequest.getAccountHolderName());
        accountEntity.setAccount_Type(accountRequest.getAccountType());
        accountEntity.setBalance(0.0);
        accountEntity.setAccountNumber("ACC-" + UUID.randomUUID());
        //saved account
        AccountEntity savedAccount=accountService.createAccount(accountEntity);
        //build gRPC response
        AccountResponse response=AccountResponse.newBuilder()
                .setId(savedAccount.getAccountId())
                .setAccountHolderName(savedAccount.getAccount_Holder_Name())
                .setAccountType(savedAccount.getAccount_Type())
                .setBalance(savedAccount.getBalance())
                .setAccountNumber(savedAccount.getAccountNumber())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    public void getAccountById(AccountRequestId accountRequestId, StreamObserver<AccountResponse> responseObserver){
        AccountEntity accountEntity=accountService.getAccountById(accountRequestId.getId());
        if(accountEntity==null){
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Account not found")
                            .asRuntimeException()
            );
            return;
        }
        AccountResponse response=AccountResponse.newBuilder()
                .setId(accountEntity.getAccountId())
                .setAccountNumber(accountEntity.getAccountNumber())
                .setAccountHolderName(accountEntity.getAccount_Holder_Name())
                .setAccountType(accountEntity.getAccount_Type())
                .setBalance(accountEntity.getBalance())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    public void getAccountByNumber(AccountNumber accountRequestId, StreamObserver<AccountResponse> responseObserver){
        try{
        String accountNumber=accountRequestId.getAccountNumber();
        if(accountNumber.trim().isEmpty()){
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Account number cannot be null")
                            .asRuntimeException()
            );
            return;

        }
        AccountEntity accountEntity=accountService.getAccountByNumber(accountRequestId.getAccountNumber());
        AccountResponse response=AccountResponse.newBuilder()
                .setId(accountEntity.getAccountId())
                .setAccountNumber(accountEntity.getAccountNumber())
                .setAccountHolderName(accountEntity.getAccount_Holder_Name())
                .setAccountType(accountEntity.getAccount_Type())
                .setBalance(accountEntity.getBalance())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }catch(RuntimeException e){
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription(e.getMessage())
                                .asRuntimeException()
                );
            }
        }
    @Override
    public void getAllAccount(Empty request, StreamObserver<AccountResponse> responseObserver) {
        List<AccountEntity> accounts=accountService.getAllAccount();
        for(AccountEntity entity:accounts){
            AccountResponse response = AccountResponse.newBuilder()
                    .setId(entity.getAccountId())
                    .setAccountNumber(entity.getAccountNumber())
                    .setAccountHolderName(entity.getAccount_Holder_Name())
                    .setAccountType(entity.getAccount_Type())
                    .setBalance(entity.getBalance())
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }
}


