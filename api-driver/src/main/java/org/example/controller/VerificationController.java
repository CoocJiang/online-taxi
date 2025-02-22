package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.remote.ServiceVefificationcodeClient;
import org.example.request.VerificationCodeDTO;
import org.example.response.NumberCodeResponse;
import org.example.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VerificationController {


    @Autowired
    ServiceVefificationcodeClient serviceVefificationcodeClient;

    @Autowired
    VerificationCodeService verificationCodeService;


    /**
     * 司机登录获取验证码
     * @param verificationCodeDTO
     * @return
     */
    @PostMapping("/verification-code")
    public ResponseResult<NumberCodeResponse> verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        return verificationCodeService.generatorCode(verificationCodeDTO);
    }

    /**
     * 检查验证码
     * @param verificationCodeDTO
     * @return
     */
    @PostMapping("/verification-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String Numbercode = verificationCodeDTO.getNumbercode();
        String Phonenumber = verificationCodeDTO.getDriverPhone();
        return  verificationCodeService.CheckVerification(Phonenumber,Numbercode);
    }

    //不拦截的路径
    @PostMapping("/noaoth")
    public String noaoth(){
        return "noaoth";
    }

    @PostMapping("/aoth")
    public String aoth(){
        return "aoth";
    }
}
