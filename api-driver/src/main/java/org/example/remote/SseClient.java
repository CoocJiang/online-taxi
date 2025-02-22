package org.example.remote;


import org.example.utils.SseUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient("service-client")
public interface SseClient {



    /**
     * 发送信息
     * @param UserId
     * @param identity
     * @param content
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam("UserId") String UserId,
                       @RequestParam ("identity")String identity,
                       @RequestParam("content") String content);

    /**
     * 关闭前端连接
     * @param UserId
     * @param identity
     * @return
     */
    @GetMapping("/close")
    public String close(@RequestParam("UserId") String UserId,@RequestParam("identity") String identity);
}
