package org.example.dao;


import lombok.Data;

@Data
public class PayRequest {
    private String subject;
    private String outTradeNo;
    private String totalAmount;

    // 省略构造函数、getter 和 setter 方法
}
