package org.example;


import com.alibaba.csp.sentinel.cluster.server.ClusterTokenServer;
import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main10002  {
    static {
        //设置参数和sentinel进行连接
        System.setProperty("csp.sentinel.dashboard.server","localhost:8080");
        System.setProperty("project.name","sentinel-tokem-server");
    }

    public static void main(String[] args) throws Exception {
        ClusterTokenServer clusterTokenServer = new SentinelDefaultTokenServer();
        ClusterServerConfigManager.loadGlobalTransportConfig(new ServerTransportConfig().setPort(10002));
        clusterTokenServer.start();
    }
}