package org.example.remote;



import org.example.dto.ResponseResult;
import org.example.response.NumberCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-verification",fallbackFactory = ServiceVerificationFallback.class)
public interface ServiceVefificationcodeClient {
    @PostMapping("/numbercode/6")
    public ResponseResult<NumberCodeResponse> numbbercode();
}

