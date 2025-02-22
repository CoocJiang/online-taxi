package org.example.remote;


import org.example.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {
    /**
     * 查询当前城市可用司机
     * @param cityCode
     * @return
     */
    @PostMapping("/isAvailableDriver")
    public boolean isAvailableDriver(@RequestParam(name = "cityCode") String cityCode);


    //查询可接单的司机
    @PostMapping("/AvailableDriver/get")
    public ResponseResult getAvailableDriver(@RequestParam(name = "carId") Long carId);


    /**
     * 根据司机id查询司机绑定信息
     * @param driverId
     * @return
     */
    @PostMapping("/getDriverCarRelationShip")
    public ResponseResult getDriverCarRelationShip(@RequestParam(name = "driverId") String driverId);
}
