package org.example.utils;





import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.dto.TokenResult;

import java.util.Date;
import java.util.HashMap;

public class JWTUtils {
    private  static final  String  SECRET = "jiang";//设置签名密钥

    private  static final String JWT_KEY_PHONE = "passengerPhoen";
    private  static final String JWT_KEY_IDENTiTY = "identity";
    //Token生成
    public static String createJWT(String passengerPhoen,String identity){
        HashMap<String,Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");
        //得到当前的系统时间
        Date currentDate = new Date();
        //根据当前时间计算出过期时间 定死为5分钟
        Date expTime = new Date(currentDate.getTime() + (1000 * 60 * 60*24*30));

        String token = JWT.create().withHeader(map)//创建头部,可以写，可以不写
                .withClaim(JWT_KEY_PHONE, passengerPhoen) //创建中间部分自定义信息
                .withClaim(JWT_KEY_IDENTiTY,identity)
                .withClaim("Cueent_time",currentDate)
//                .withExpiresAt(expTime)//定义过期时间
                .sign(Algorithm.HMAC256(SECRET));//使用HMAC256进行校验，后面的是签名
        return token;
    }
    //TOken解析
    public static TokenResult parseToken(String token ){
        try {
            //获取JWT解析对象，传入需要解析的密钥以及方式
            JWTVerifier verifierJWT = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //传入需要解析的token，获得解析之后的数据
            DecodedJWT decodedJWT = verifierJWT.verify(token);
            //从解析之后的数据提取需要的信息
            String phone = decodedJWT.getClaim(JWT_KEY_PHONE).asString();
            String identity = decodedJWT.getClaim(JWT_KEY_IDENTiTY).asString();
            return new TokenResult(identity,phone);
        }catch (Exception e){
            return null;
        }
    }

}
