package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.example.dto.PriceRule;
import org.example.dto.ResponseResult;
import org.example.mapper.PriceRuleMapper;
import org.example.remote.ServiceMapClient;
import org.example.request.ForecastPriceDTO;
import org.example.response.DirectionResponse;
import org.example.response.ForecastPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ForecastPriceService {
    @Resource
    ServiceMapClient serviceMapClient;

    @Autowired
    PriceRuleMapper priceRuleMapper;

    //调用service-map微服务获得距离和时间，从数据库获取计价规则，调用计算价格方法
    public ResponseResult getPrice(ForecastPriceDTO forecastPriceDTO ) {

        ResponseResult forecastprice = serviceMapClient.forecastprice(forecastPriceDTO);
        Object data = forecastprice.getData();
        Map<String, Object> dataMap = (Map<String, Object>) data;
        //获取高德接口的距离和时间
        Integer distance = (int) dataMap.get("distance");
        Integer duration = (int) dataMap.get("duration");
        Integer price =(int) dataMap.get("price");
        //从数据库获取价格表
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",forecastPriceDTO.getCityCode())
                .eq("vehicle_type",forecastPriceDTO.getVehicleType())
                .orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        //根据距离和时间计算价格，price为预估出租车价格
        log.info("从数据库获取价格表，按照价格表计算价格返回,距离为"+distance.toString()+"时间为"+duration.toString());
        log.info("高德地图预估价格为"+price.toString());
        //按照价格表计算价格返回
        Double lastprice =  countprice(distance,priceRules.get(0),duration);
        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(lastprice);
        forecastPriceResponse.setCityCode(forecastPriceDTO.getCityCode());
        forecastPriceResponse.setVehicleType(forecastPriceDTO.getVehicleType());
        forecastPriceResponse.setFareType(priceRules.get(0).getFareType());
        forecastPriceResponse.setFareVersion(priceRules.get(0).getFareVersion());
        return ResponseResult.success(forecastPriceResponse);
    }

    //设计到金钱的数据尽量使用Bigdecimal计算
    private double countprice(Integer distance,PriceRule priceRule,Integer duration){
        //起步里程
        BigDecimal startmile = new BigDecimal(priceRule.getStartMile());
//        log.info(startmile.toString());
        //起步价
        BigDecimal startfare = BigDecimal.valueOf(priceRule.getStartFare());
//        log.info(startfare.toString());
        //里程价格
        BigDecimal mile_fare = BigDecimal.valueOf(priceRule.getUnitPricePerMile());
//        log.info(mile_fare.toString());
        //分钟价格
        BigDecimal min_fare = BigDecimal.valueOf(priceRule.getUnitPricePerMinute());
        //总里程
        BigDecimal distanceall = new BigDecimal(distance);
        BigDecimal distanceallkm = distanceall.divide(new BigDecimal(1000),2,BigDecimal.ROUND_UP);
//        log.info(distanceallkm.toString());
        //如果小于起步距离
        if(distanceallkm.doubleValue()<startmile.doubleValue()){
            return startfare.doubleValue();
        }else {
            BigDecimal part  = distanceallkm.subtract(startmile);
//            log.info(part.toString());
            //返回最终价格
            double first_fare= mile_fare.multiply(part).doubleValue();
            double second_fare=min_fare.multiply(new BigDecimal(duration).divide(new BigDecimal(60),2,BigDecimal.ROUND_UP)).doubleValue();


            return startfare.doubleValue()+first_fare+second_fare;
        }
    }

}
