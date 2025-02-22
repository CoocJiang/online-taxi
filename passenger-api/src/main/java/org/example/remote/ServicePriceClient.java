package org.example.remote;

import org.example.dto.ResponseResult;
import org.example.request.ForecastPriceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-price")
public interface ServicePriceClient {

    @PostMapping("/forecast-price")
    public ResponseResult forecastprice(@RequestBody ForecastPriceDTO forecastPriceDTO);
}
