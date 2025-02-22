package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TokenResult;
import org.example.utils.JWTUtils;
import org.example.utils.RedisPrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MyglobalFilter implements GlobalFilter, Ordered {

    @Autowired
    PathInterceptorConfig pathInterceptorConfig ;
//    @Autowired
//    StringRedisTemplate redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> NoAuthpaths = pathInterceptorConfig.getNoAuthPaths();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getPath().toString();
        TokenResult tokenResult = null;
        String key = null;
        boolean flag = true;
        if (!NoAuthpaths.contains(path)){
            log.info("需要校验的路径"+path);
//            //获取请求带来的token
//            String token  = request.getHeaders().getFirst("Authorization");
//            log.info("token验证");
//            tokenResult = JWTUtils.parseToken(token);
//            if (tokenResult!=null){
//                String phone = tokenResult.getPhone();
//                String identity = tokenResult.getIdentity();
//                key =  RedisPrefixUtils.gennerateTokenKey(phone,identity);
//                String newtoken = (String) redisTemplate.opsForValue().get(key);
//                log.info("查询的用户token:"+newtoken);
//                if (newtoken!=null&&newtoken.equals(token)){
//                    log.info("token验证通过");
//                    flag = true;
//                }else {
//                    log.info("toekn验证失败");
//                    flag = false;
//                }
//            }else {
//                log.info("toekn验证失败");
//                flag = false;
//            }
        }else{
            log.info("不需要校验的路径"+path);
        }
        if (flag) {
            return chain.filter(exchange);
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    //数字越小优先级越高
    @Override
    public int getOrder() {
        return -1;
    }
}
