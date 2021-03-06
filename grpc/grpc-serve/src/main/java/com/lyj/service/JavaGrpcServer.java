package com.lyj.service;

import com.lyj.CommonServiceGrpc;
import com.lyj.Request;
import com.lyj.Response;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author lyj
 */
@GrpcService
@Slf4j
public class JavaGrpcServer extends CommonServiceGrpc.CommonServiceImplBase {

    @Override
    public void method(Request req, StreamObserver<Response> responseObserver) {
        log.info("request;{}", req.getRequest());
        Response reply = Response.newBuilder().setResponse("Hello ==>").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
