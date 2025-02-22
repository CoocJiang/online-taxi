package org.example.remote;


import org.example.dto.ResponseResult;
import org.example.request.ForecastPriceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-price")
public interface ServicePriceClient {


    @GetMapping("/price/isnew")
    //判断是否为最新的计价规则
    public ResponseResult isnewpricerule(@RequestParam(name = "fareType") String fareType,
                                         @RequestParam(name = "fareVersion")String fareVersion);

    @GetMapping("/pricerule/getnew")
    //查询最新的计价规则
    public ResponseResult selectnewpricerule(@RequestParam(name = "fareType") String fareType,
                                             @RequestParam(name = "fareVersion")String fareVersion);

    @GetMapping("/price/isexist")
    //判断当前城市是否有计价规则
    public boolean isexistrule(@RequestParam(name = "cityCode") String cityCode,
                               @RequestParam(name = "vehicleType") String vehicleType);

    /**
     * 获取预估价格
     * @param forecastPriceDTO
     * @return
     */
    @PostMapping("/forecast-price")
    public ResponseResult forecastprice(@RequestBody ForecastPriceDTO forecastPriceDTO);
}
