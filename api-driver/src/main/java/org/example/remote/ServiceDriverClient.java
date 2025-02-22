package org.example.remote;


import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.DriverUser;
import org.example.dto.DriverUserWorkStatus;
import org.example.dto.ResponseResult;
import org.example.request.VerificationCodeDTO;
import org.example.response.OrderDriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-driver-user")
public interface ServiceDriverClient {
    //更新司机信息
     @PostMapping("/user/update")
    public ResponseResult uodateDriver(@RequestBody DriverUser driverUser);

    //根据手机号判断司机是否存在
    @PostMapping("/findDriverByPhone")
    public ResponseResult findDriverByPhone(@RequestBody VerificationCodeDTO verificationCodeDTO);


    @PostMapping("/getcarById")
    //获取car的id
    public ResponseResult gatCarbyId(@RequestParam("carId") Long carId);

    //查询可接单的司机
    @PostMapping("/AvailableDriver/get")
    public ResponseResult <OrderDriverResponse> getAvailableDriver(@RequestParam(name = "carId") Long carId);


    /**
     * 根据司机手机号查询司机绑定信息
     * @param driverPhone
     * @return
     */
    @PostMapping("/getDriverCarRelationShipByDriverPhone")
    public ResponseResult<DriverCarBindingRelationship>  getDriverCarRelationShipByDriverPhone(
            @RequestParam("driverPhone") String driverPhone);

    /**
     * 修改司机工作状态
     * @param driverUserWorkStatus
     * @return
     */
    @PostMapping("/driver-user-work-status")
    public ResponseResult ChangeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus);
}
