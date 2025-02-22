package org.example.remote;

import org.example.dto.ResponseResult;
import org.example.response.NumberCodeResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class ServiceVerificationFallback implements FallbackFactory<ServiceVefificationcodeClient> {


    @Override
    public ServiceVefificationcodeClient create(Throwable cause) {

        return new ServiceVefificationcodeClient() {
            @Override
            public ResponseResult<NumberCodeResponse> numbbercode(){
                NumberCodeResponse numberCodeResponse = new NumberCodeResponse();
                numberCodeResponse.setNumbercode(111111);
                return ResponseResult.success(numberCodeResponse);
            }
        };
    }
}
