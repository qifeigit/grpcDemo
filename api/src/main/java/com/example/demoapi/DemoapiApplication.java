package com.example.demoapi;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.examples.lib.HelloReply;
import net.devh.boot.grpc.examples.lib.HelloRequest;
import net.devh.boot.grpc.examples.lib.MyServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.devh.boot.grpc.client.inject.GrpcClient;


@SpringBootApplication
@RestController
public class DemoapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoapiApplication.class, args);
    }


    @GrpcClient("local-grpc-server")
    private MyServiceGrpc.MyServiceBlockingStub greeterStub;

    private MyServiceGrpc.MyServiceBlockingStub helloWorldServiceBlockingStub;


    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        try {
            ManagedChannel managedChannel = ManagedChannelBuilder
                    .forAddress("localhost", 9898).usePlaintext().build();
            helloWorldServiceBlockingStub =
                    MyServiceGrpc.newBlockingStub(managedChannel);
            final HelloReply response = helloWorldServiceBlockingStub.sayHello(HelloRequest.newBuilder(HelloRequest.getDefaultInstance()).setName(name).build());
            return response.getMessage();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }
//        return myServiceStub.sayHello(request).getMessage();
//        return String.format("Hello %s!", name);
    }
}
