package org.example.remote;

import org.example.dto.ResponseResult;
import org.example.request.VerificationCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-passenger-user")
public interface ServicePassengerUserClient {
    //查询用户是否存在，若不存在就注册，存在就do nothing
    @PostMapping("/user")
    public ResponseResult LoginOrRes(@RequestBody VerificationCodeDTO verificationCodeDTO);


    //获取用户信息
    @GetMapping("/getuser{passengerPhone}")
    public ResponseResult getuser(@PathVariable ("passengerPhone") String passengerPhone);
}
