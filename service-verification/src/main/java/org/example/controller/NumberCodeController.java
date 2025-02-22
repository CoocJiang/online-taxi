package org.example.controller;



import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import net.sf.json.JSONObject;
import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.example.response.NumberCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NumberCodeController {

    /**
     * 生成验证码
     * @param size
     * @return
     */
    @PostMapping("/numbercode/{size}")
    @SentinelResource(value = "numbercode",blockHandler = "numbercodeHandler",fallback = "numbercodeFallback")
    public ResponseResult numbbercode(@PathVariable("size") int size){
        int nums = (int) ((Math.random()*9+1)*(Math.pow(10,size-1)));
        JSONObject data = new JSONObject();
        data.put("numbercode",nums);
        NumberCodeResponse numberCodeResponse = new NumberCodeResponse();
        numberCodeResponse.setNumbercode(nums);
        return ResponseResult.success(numberCodeResponse);
    }
    //处理BlockException的函数名称,函数要求：
    //1. 必须是 public
    //2.返回类型 参数与原方法一致
    //3. 默认需和原方法在同一个类中。若希望使用其他类的函数，可配置blockHandlerClass ，并指定blockHandlerClass里面的方法。
    public ResponseResult numbercodeHandler(@PathVariable("size") int size, BlockException e){
        return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(),"自定义限流");
    }


    //用于通用的 fallback 逻辑。默认fallback函数可以针对所有类型的异常进
    //行处理。若同时配置了 fallback 和 defaultFallback，以fallback为准。函数要求：
    //1. 返回类型与原方法一致
    //2. 方法参数列表为空，或者有一个 Throwable 类型的参数。
    //3. 默认需要和原方法在同一个类中。若希望使用其他类的函数，可配置fallbackClass ，并指定 fallbackClass 里面的方法。
    public ResponseResult numbercodeFallback(@PathVariable("size") int size, Throwable e){
        //服务异常调用的
        return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(),"服务异常");
    }
}
