package org.example.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
public class VerificationCodeDTO {


    String numbercode;

    String passengerPhone;

    String driverPhone;
    public VerificationCodeDTO() {
        // 默认构造函数内容
    }
}
