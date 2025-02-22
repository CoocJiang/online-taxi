package org.example.config;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class ZookeeperCruatorConfig {
//
//
//    @Value("${zookeeper.address}")
//    String address;
//
//    @Value("${zookeeper.timeout}")
//    int timeout;
//
//    @Bean
//    public CuratorFramework curatorFramework(){
//
//        //定义重试策略
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);
//
//        //利用CuratorFramework连接zookeeper
//        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(address,retryPolicy);
//
//        curatorFramework.start();
//
//        return curatorFramework;
//
//    }
//}
