//package org.example.interceptor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class IterceptorConfig implements WebMvcConfigurer {
//    @Autowired
//    JWTinterceptor jwTinterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//       registry.addInterceptor(jwTinterceptor)
//               .addPathPatterns("/**")//拦截的路径
//               //不拦截的路径
//               .excludePathPatterns("/noaoth","/verification-code","/verification-code-check");
//    }
//}
