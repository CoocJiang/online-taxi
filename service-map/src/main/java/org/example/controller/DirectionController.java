package org.example.controller;

import org.example.dto.ResponseResult;
import org.example.request.ForecastPriceDTO;
import org.example.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/direction")
public class DirectionController {
    @Autowired
    DirectionService directionService;

    @PostMapping("/driving")
    public ResponseResult forecastprice(@RequestBody ForecastPriceDTO forecastPriceDTO){
        String depLatitude = forecastPriceDTO.getDepLatitude();
        String depLongitude = forecastPriceDTO.getDepLongitude();
        String destLongitude = forecastPriceDTO.getDestLongitude();//获取经度
        String destLatitude = forecastPriceDTO.getDestLatitude();//获取纬度
        return directionService.driving(depLongitude,depLatitude,destLongitude,destLatitude);
    }
}
