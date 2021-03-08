package com.lyj.grpc.service;

import com.lyj.CommonServiceGrpc;
import com.lyj.Request;
import com.lyj.Response;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * @author lyj
 */
@Service
@Slf4j
public class GrpcClientService {

	@GrpcClient("local-grpc-server")
	private CommonServiceGrpc.CommonServiceBlockingStub simpleStub;

	public String sendMessage(final String name) {
		try {
			final Response response = this.simpleStub.method(Request.newBuilder().setRequest(name).build());
			return response.getResponse();
		}
		catch (final StatusRuntimeException e) {
			return "FAILED with " + e.getStatus().getCode().name();
		}
	}

}
