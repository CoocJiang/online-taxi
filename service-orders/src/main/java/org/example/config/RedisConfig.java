package org.example.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;


@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    String host;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":6379").setDatabase(0);

        return Redisson.create(config); // 传递配置对象给Redisson.create()方法
    }

    /**
     * redis序列化的工具配置类，下面这个请一定开启配置
     * 127.0.0.1:6379> keys *
     * 1) "ord:102"  序列化过
     * 2) "\xac\xed\x00\x05t\x00\aord:102"   野生，没有序列化过
     * this.redisTemplate.opsForValue(); //提供了操作string类型的所有方法
     * this.redisTemplate.opsForList(); // 提供了操作list类型的所有方法
     * this.redisTemplate.opsForSet(); //提供了操作set的所有方法
     * this.redisTemplate.opsForHash(); //提供了操作hash表的所有方法
     * this.redisTemplate.opsForZSet(); //提供了操作zset的所有方法
     *
     * @param lettuceConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate (LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        //设置连接方式
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        //设置key的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化放肆
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean("redissetScript")
    //配置的lua脚本实现对redis的操作，但是项目用的redisson 仅仅用于demo熟悉lua脚本配合redis
    public DefaultRedisScript<Boolean> redissetScript(){
        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<Boolean>();
        //设置读取的lua脚本的路径
        defaultRedisScript.setScriptSource
                (new ResourceScriptSource(new ClassPathResource("luascript/redis-set.lua")));
        //设置返回值类型为布尔值
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }
}


