//package org.example.request;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//
//@Data
//@AllArgsConstructor
//public class CheckVerificationCodeDTO {
//
//    @NotBlank(message = "请填写验证码")
//    @Pattern(regexp = "^\\d{6}$",message = "验证码错误")
//    String numbercode;
//
//    String passengerPhone;
//
//    String driverPhone;
//    public CheckVerificationCodeDTO() {
//        // 默认构造函数内容
//    }
//}
