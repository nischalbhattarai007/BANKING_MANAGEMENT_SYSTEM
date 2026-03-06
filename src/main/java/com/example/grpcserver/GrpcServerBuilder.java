package com.example.grpcserver;

import com.example.endpoint.AccountServiceEndPoint;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Singleton;

@Singleton
public class GrpcServerBuilder implements BeanCreatedEventListener<ServerBuilder<?>> {
    private final AccountServiceEndPoint accountServiceEndPoint;

    public GrpcServerBuilder(AccountServiceEndPoint accountServiceEndPoint) {
        this.accountServiceEndPoint = accountServiceEndPoint;
    }
    public ServerBuilder<?> onCreated(
            BeanCreatedEvent<ServerBuilder<?>> event
    ){
        ServerBuilder<?> builder=event.getBean();
        //convert grpc server to server service definition
        ServerServiceDefinition serviceDefinition=accountServiceEndPoint.bindService();
        //manually registered service
        builder.addService(serviceDefinition);
        return builder;
    }
}
