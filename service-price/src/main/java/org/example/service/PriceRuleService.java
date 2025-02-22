package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.constant.CommonStatusEnum;
import org.example.dto.PriceRule;
import org.example.dto.ResponseResult;
import org.example.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PriceRuleService {

    @Autowired
    PriceRuleMapper priceRuleMapper;

    public ResponseResult addpricerule(PriceRule priceRule){

        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();

        String fareType = cityCode+"$"+vehicleType;
        //设置faretype
        priceRule.setFareType(fareType);

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode)
                        .eq("vehicle_type",vehicleType)
                        .orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        //如果之前没有计价规则的版本，默认为1
        Integer fareversion = 0;
        if (priceRules.size()>0){
            //如果有计价规则，不允许新增
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EXISTS.getCode(),
                    CommonStatusEnum.PRICE_RULE_EXISTS.getValue());
        }
        priceRule.setFareVersion(++fareversion);
        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }
    public ResponseResult editpricerule(PriceRule priceRule){

        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();

        String fareType = cityCode+"$"+vehicleType;
        //设置faretype
        priceRule.setFareType(fareType);

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode)
                .eq("vehicle_type",vehicleType)
                .orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        //如果之前没有计价规则的版本，默认为1
        Integer fareversion = 0;
        if (priceRules.size()>0){
            //如果有计价规则，不允许新增
            PriceRule priceRule1 = priceRules.get(0);
            if (Objects.equals(priceRule1.getStartMile(), priceRule.getStartMile())
                    && Objects.equals(priceRule1.getStartFare(), priceRule.getStartFare())
                    && Objects.equals(priceRule1.getUnitPricePerMile(), priceRule.getUnitPricePerMile())
                    && Objects.equals(priceRule1.getUnitPricePerMinute(), priceRule.getUnitPricePerMinute())
            ){
                return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_NOT_EDIT.getCode(),
                        CommonStatusEnum.PRICE_RULE_NOT_EDIT.getValue());
            }else {
                fareversion=priceRule1.getFareVersion();
            }
        }
        priceRule.setFareVersion(++fareversion);
        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }


    public ResponseResult selectnewpricerule(String fareType, String fareVersion) {

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fare_type",fareType)
                .eq("fare_version",fareVersion);

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);

        if (priceRules.size()==0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),
                    CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }else {
            PriceRule priceRule = priceRules.get(0);
            return ResponseResult.success(priceRule);
        }
    }


    public ResponseResult isnewpricerule(String fareType, String fareVersion) {
        ResponseResult<PriceRule> selectnewpricerule = selectnewpricerule(fareType, fareVersion);
        if (selectnewpricerule.getCode() == CommonStatusEnum.PRICE_RULE_EMPTY.getCode()){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),
                    CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }else {
            Integer fareVersion1 = selectnewpricerule.getData().getFareVersion();

            if (Integer.parseInt(fareVersion)>=fareVersion1){
                return ResponseResult.success(true);
            }else {
                return ResponseResult.success(false);
            }
        }
    }

    public boolean isexistrule(String cityCode, String vehicleType) {
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode)
                .eq("vehicle_type",vehicleType)
                .orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size()>0){
            return true;
        }else {
            return false;
        }
    }
}
