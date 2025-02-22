package org.example;


import lombok.extern.slf4j.Slf4j;
import org.example.utils.SseUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class SseController {
    public static Map<String,SseEmitter> sseEmitterMap= new HashMap<>();

    /**
     * 建立与前端的连接
     * @param UserId
     * @param identity
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam String UserId,@RequestParam String identity){
        String id = SseUtils.gennerate(UserId, identity);
        log.info(id+"连接成功");
        //设置连接用不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
        sseEmitterMap.put(id,sseEmitter);
        return sseEmitter;
    }


    /**
     * 发送信息
     * @param UserId
     * @param identity
     * @param content
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam String UserId,@RequestParam String identity,@RequestParam String content){
        String id;
        log.info(UserId);
        try {id = SseUtils.gennerate(UserId, identity);
            log.info("向id为"+id+"发送信息");
            if (sseEmitterMap.containsKey(id)){
                sseEmitterMap.get(id).send(content);
            }else {
                log.info("连接已关闭");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "给用户"+id+"发送了消息";
    }


    @GetMapping("/pushprice")
    public String pushprice(@RequestParam String UserId, @RequestParam String identity,
                            @RequestParam String price,@RequestParam String OrderId) {
        String id;
        log.info(UserId);
        try {
            id = SseUtils.gennerate(UserId, identity);
            // 构造超链接格式的消息内容
            String content = "<a href='http://localhost:9010/alipay/pay?subject=fare&traceNo="+OrderId+"&totalAmount="+price+"'>Click here to pay</a>";

            if (sseEmitterMap.containsKey(id)) {
                sseEmitterMap.get(id).send(content);
            } else {
                log.info("连接已关闭");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "给用户" + id + "发送了消息";
    }

    /**
     * 关闭前端连接
     * @param UserId
     * @param identity
     * @return
     */
    @GetMapping("/close")
    public String close(@RequestParam String UserId,@RequestParam String identity){
        String id = SseUtils.gennerate(UserId, identity);
        if (sseEmitterMap.containsKey(id)){
            sseEmitterMap.remove(id);
            log.info("关闭成功");
        }
            return "<p>关闭成功</p>";
    }
}
