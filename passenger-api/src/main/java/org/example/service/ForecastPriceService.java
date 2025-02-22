package org.example.service;


import org.example.dto.ResponseResult;
import org.example.remote.ServicePriceClient;
import org.example.request.ForecastPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForecastPriceService {
    @Autowired
    ServicePriceClient servicePriceClient;
    public ResponseResult getPrice(ForecastPriceDTO forecastPriceDTO) {
        return servicePriceClient.forecastprice(forecastPriceDTO);
    }
}
