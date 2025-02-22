package org.example.service;


import net.sf.json.JSONObject;
import org.example.constant.IdentityConstants;
import org.example.dto.OrderInfo;
import org.example.dto.ResponseResult;
import org.example.remote.OrderServiceClient;
import org.example.remote.SseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {

    @Autowired
    SseClient sseClient;

    @Autowired
    OrderServiceClient orderServiceClient;



    public ResponseResult pushPayInfo(String orderId){
        ResponseResult result = orderServiceClient.toStartPay(orderId);
        JSONObject jsonObject = JSONObject.fromObject(result.getData());
        String passengerId = jsonObject.getString("id");
        String price = jsonObject.getString("price");;
        sseClient.push("123121221", IdentityConstants.PASSENGER_IDENTITY, String.valueOf(price));
        return ResponseResult.success();
    }
}
