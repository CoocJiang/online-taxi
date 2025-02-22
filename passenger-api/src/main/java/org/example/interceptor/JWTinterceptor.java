//package org.example.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.constant.CommonStatusEnum;
//import org.example.dto.ResponseResult;
//import org.example.dto.TokenResult;
//import org.example.utils.JWTUtils;
//import org.example.utils.RedisPrefixUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class JWTinterceptor implements HandlerInterceptor {
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    TokenResult tokenResult;
//
//    String key;
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //获取请求带来的token
//        String token = request.getHeader("Authorization");
//        log.info("token验证");
////        System.out.println("拦截器得到的header里的token"+token);
//        tokenResult = JWTUtils.parseToken(token);
////        System.out.println("解析得到tokenresult"+tokenResult);
//        if (tokenResult!=null){
//            String phone = tokenResult.getPhone();
//            String identity = tokenResult.getIdentity();
//            key =  RedisPrefixUtils.gennerateTokenKey(phone,identity);
//            String newtoken = (String) redisTemplate.opsForValue().get(key);
//            log.info("查询的用户token:"+newtoken);
//            if (newtoken!=null&&newtoken.equals(token)){
//                log.info("token验证通过");
//                return true;
//            }
//        }
//            response.getWriter().println(ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(), CommonStatusEnum.TOKEN_ERROR.getValue()));
//            log.info("toekn验证失败");
//            return false;
//    }
//    //实现toekn刷新，提升用户体验
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 添加额外的数据到HttpServletResponse
//        log.info("执行posthandle");
//        Long expire = redisTemplate.getExpire(key, TimeUnit.HOURS);
//        if (expire<72){
//            String refreshtoken = JWTUtils.createJWT(tokenResult.getPhone(),tokenResult.getIdentity());
//            redisTemplate.opsForValue().set(key,refreshtoken,30,TimeUnit.DAYS);
//            log.info("更新token成功");
//            response.addHeader("Authorization", refreshtoken);
//        }
//    }
//
//}
