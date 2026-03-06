package com.example.endpoint;

import com.example.*;
import com.example.entity.TransactionEntity;
import com.example.services.TransactionService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

public class TransactionServiceEndPoint extends TransactionServiceGrpc.TransactionServiceImplBase {
    private final TransactionService transactionService;
    @Inject
    public TransactionServiceEndPoint(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void deposit(DepositRequest request, StreamObserver<TransactionResponse> responseObserver) {
        try{
        TransactionEntity txn=transactionService.deposit(request.getAccountNumber(),request.getAmount());
        TransactionResponse response=TransactionResponse.newBuilder()
                .setTransactionId(txn.getTransactionId())
                .setAmount(txn.getAmount())
                .setStatus("Success")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    catch(RuntimeException e){
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
    }
}

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<TransactionResponse> responseObserver) {
       try{
           TransactionEntity txn=transactionService.withdraw(request.getAccountNumber(),request.getAmount());
           TransactionResponse response=TransactionResponse.newBuilder()
                   .setTransactionId(txn.getTransactionId())
                   .setAmount(txn.getAmount())
                   .setStatus("Success")
                   .build();
           responseObserver.onNext(response);
           responseObserver.onCompleted();
       }
       catch(RuntimeException e){
           responseObserver.onError(
                   Status.FAILED_PRECONDITION
                           .withDescription(e.getMessage())
                           .asRuntimeException()
           );
       }
    }

    @Override
    public void transfer(TransferRequest request, StreamObserver<TransactionResponse> responseObserver) {
        try{
            transactionService.transfer(
                    request.getFromAccountNumber(),
                    request.getToAccountNumber(),
                    request.getAmount()
            );
            TransactionResponse response=TransactionResponse.newBuilder()
                    .setAmount(request.getAmount())
                    .setStatus("SUCCESS")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch (RuntimeException e){
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
