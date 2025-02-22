package org.example.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.example.constant.IdentityConstants;
import org.example.dto.PassengerUser;
import org.example.dto.ResponseResult;
import org.example.remote.ServiceOrderClient;
import org.example.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class OrderService {

    @Autowired
    ServiceOrderClient serviceOrderClient;

    @Autowired
    UserService userService;

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    public  ResponseResult cancel(Long orderId) {
        return serviceOrderClient.cancel(orderId,IdentityConstants.PASSENGER_IDENTITY);
    }


    /**
     * 用户下订单
     * @param orderRequest
     * @param request
     * @return
     */
    public ResponseResult addorder(OrderRequest orderRequest, HttpServletRequest request) {
        String stoken =  request.getHeader("Authorization");
        ResponseResult<PassengerUser> result = userService.getUsertoken(stoken);
        JSONObject jsonObject = JSONObject.fromObject(result);
        orderRequest.setPassengerPhone(jsonObject.getJSONObject("data").getString("passengerPhone"));
        orderRequest.setPassengerId(Long.valueOf(jsonObject.getJSONObject("data").getString("id")));
        return serviceOrderClient.addorder(orderRequest);
    }


    /**
     * 查询订单信息
     * @param orderId
     * @return
     */
    public ResponseResult OrderDetail(Long orderId) {
        return null;
    }


    /**
     * 预约订单
     * @param orderRequest
     * @param request
     * @return
     */
    public ResponseResult bookorder(OrderRequest orderRequest, HttpServletRequest request) {
        String stoken =  request.getHeader("Authorization");
        ResponseResult<PassengerUser> result = userService.getUsertoken(stoken);
        JSONObject jsonObject = JSONObject.fromObject(result);
        orderRequest.setPassengerPhone(jsonObject.getJSONObject("data").getString("passengerPhone"));
        orderRequest.setPassengerId(Long.valueOf(jsonObject.getJSONObject("data").getString("id")));
        return serviceOrderClient.bookorder(orderRequest);
    }
}
