package org.example.controller;


import org.example.dto.PriceRule;
import org.example.dto.ResponseResult;
import org.example.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PriceRuleController {

    @Autowired
    PriceRuleService priceRuleService;


    //添加计价规则
    @PostMapping("/pricerule/add")
    public ResponseResult addpricerule(@RequestBody PriceRule priceRule){
        return  priceRuleService.addpricerule(priceRule);
    }
    //修改计价规则
    @PostMapping("/pricerule/edit")
    public ResponseResult editpricerule(@RequestBody PriceRule priceRule){
        return  priceRuleService.editpricerule(priceRule);
    }


    @GetMapping("/pricerule/getnew")
    //查询最新的计价规则
    public ResponseResult selectnewpricerule(@RequestParam String fareType,@RequestParam String fareVersion){

       return priceRuleService.selectnewpricerule(fareType,fareVersion);
    }

    @GetMapping("/price/isnew")
    //判断是否为最新的计价规则
    public ResponseResult isnewpricerule(@RequestParam String fareType,@RequestParam String fareVersion){
        return priceRuleService.isnewpricerule(fareType,fareVersion);
    }

    @GetMapping("/price/isexist")
    //判断当前城市是否有计价规则
    public boolean isexistrule(@RequestParam String cityCode,@RequestParam String vehicleType){
        return priceRuleService.isexistrule(cityCode,vehicleType);
    }

}
