package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.request.ForecastPriceDTO;
import org.example.service.ForecastPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastPriceController {
    @Autowired
    ForecastPriceService forecastPriceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastprice(@Validated @RequestBody ForecastPriceDTO forecastPriceDTO){
       return forecastPriceService.getPrice( forecastPriceDTO);
    }
}
