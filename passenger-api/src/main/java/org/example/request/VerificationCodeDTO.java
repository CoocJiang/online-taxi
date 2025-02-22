package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.constrains.CheckVerificationCodeGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class VerificationCodeDTO {

    @NotBlank(message = "请填写验证码",groups = {CheckVerificationCodeGroup.class})
    @Pattern(regexp = "^\\d{6}$",message = "验证码错误",groups = {CheckVerificationCodeGroup.class})
    String numbercode;

    @NotBlank(message = "请填写手机号")
    @Pattern(regexp = "^1[3,4,5,6,7,8,9]\\d{9}$" ,message = "请填写正确的手机号")
    String passengerPhone;

    String driverPhone;
    public VerificationCodeDTO() {
        // 默认构造函数内容
    }
}
