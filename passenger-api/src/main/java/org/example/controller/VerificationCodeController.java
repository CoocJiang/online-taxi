package org.example.controller;



import org.example.constrains.CheckVerificationCodeGroup;
import org.example.dto.ResponseResult;


import org.example.request.VerificationCodeDTO;
import org.example.response.NumberCodeResponse;
import org.example.service.VerificationCodeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class VerificationCodeController {
    @Resource
    private VerificationCodeService verificationCodeService;

    /**
     * 发送手机验证码
     * @param verificationCodeDTO
     * @return
     */
    @PostMapping("/verification-code")
    public ResponseResult<NumberCodeResponse> verificationCode(@Validated @RequestBody VerificationCodeDTO verificationCodeDTO){
        return verificationCodeService.generatorCode(verificationCodeDTO.getPassengerPhone());
    }

    @PostMapping("/verification-code-check")
    public ResponseResult checkVerificationCode(@RequestBody @Validated(CheckVerificationCodeGroup.class) VerificationCodeDTO verificationCodeDTO){
        String Numbercode = verificationCodeDTO.getNumbercode();
        String Phonenumber = verificationCodeDTO.getPassengerPhone();
        return  verificationCodeService.CheckVerification(Phonenumber,Numbercode);
    }

}
