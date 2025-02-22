package org.example.remote;



import org.example.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-verification")
public interface ServiceVefificationcodeClient {

    //调用验证码服务获取验证码
    @PostMapping("/numbercode/6")
    public ResponseResult numbercode();
}

