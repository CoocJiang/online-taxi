package org.example.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.seata.spring.annotation.GlobalTransactional;
import net.sf.json.JSONObject;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.DriverUserWorkStatus;
import org.example.dto.OrderInfo;
import org.example.dto.ResponseResult;
import org.example.remote.OrderServiceClient;
import org.example.remote.ServiceDriverClient;
import org.example.request.DriverGrabRequest;
import org.example.request.OrderRequest;
import org.example.response.DriverUserExistsResponse;
import org.example.response.OrderDriverResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {


    @Autowired
    OrderServiceClient orderServiceClient;

    @Autowired
    ServiceDriverClient serviceDriverClient;


    /**
     * 司机抢订单
     * @param receiveOrderCarLatitude
     * @param receiveOrderCarLongitude
     * @return
     */
    @GlobalTransactional
    public ResponseResult grab(String orderId, String phone, String receiveOrderCarLongitude, String receiveOrderCarLatitude){
        ResponseResult<DriverCarBindingRelationship> result =
                serviceDriverClient.getDriverCarRelationShipByDriverPhone(phone);
        //再次判断司机是否有绑定的车辆
        if (result==null){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXISTS.getCode(),
                    CommonStatusEnum.DRIVER_BIND_EXISTS.getValue());
        }else {
            Long carId1 = result.getData().getCarId();
            JSONObject jsonObject = JSONObject.fromObject(result.getData());
            long carId = jsonObject.getLong("carId");
            ResponseResult<OrderDriverResponse> orderDriverResult = serviceDriverClient.getAvailableDriver(carId);
            if (orderDriverResult.getData()==null){
                return ResponseResult.fail(CommonStatusEnum.CAR_NOT_EXISTS.getCode(),
                        CommonStatusEnum.CAR_NOT_EXISTS.getValue());
            }else {
//                jsonObject = JSONObject.fromObject(orderDriverResult.getData());
                String vehicleNo = orderDriverResult.getData().getVehicleNo();//("vehicleNo");
                String licenseId = orderDriverResult.getData().getLicenseId();//("licenseId");
                String vehicleType = orderDriverResult.getData().getVehicleType();//("vehicleType");
                Long driverId = orderDriverResult.getData().getDriverId();//("driverId");

                DriverGrabRequest driverGrabRequest = new DriverGrabRequest();

                driverGrabRequest.setDriverId(driverId);
                driverGrabRequest.setDriverPhone(phone);
                driverGrabRequest.setCarId(carId);
                driverGrabRequest.setLicenseId(licenseId);
                driverGrabRequest.setVehicleNo(vehicleNo);
                driverGrabRequest.setVehicleType(vehicleType);
                driverGrabRequest.setReceiveOrderCarLatitude(receiveOrderCarLatitude);
                driverGrabRequest.setReceiveOrderCarLongitude(receiveOrderCarLongitude);
                driverGrabRequest.setOrderId(Long.valueOf(orderId));

                //执行抢单动作  调用订单服务完成
                ResponseResult responseResult = orderServiceClient.grab(driverGrabRequest);
                if (responseResult==null||responseResult.getCode()!=CommonStatusEnum.SUCCESS.getCode()){
                    return responseResult;
                }
                //抢单成功之后修改司机出车状态
                DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
                driverUserWorkStatus.setGmtModified(LocalDateTime.now());
                driverUserWorkStatus.setDriverId(driverId);
                driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_WORING);
//                int s = 1/0;  //仅仅用于手动制造错测试分布式事务
                responseResult = serviceDriverClient.ChangeWorkStatus(driverUserWorkStatus);

                return ResponseResult.success().setMessage("抢单成功");
            }
        }
    }
}
