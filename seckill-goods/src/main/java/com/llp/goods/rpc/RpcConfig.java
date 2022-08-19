package com.llp.goods.rpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件的rpc信息
 */

@Component
@ConfigurationProperties(prefix = "rpc")
public class RpcConfig {
    private String serializerAlgorithm;
    private String zookeeperAddress;
    private String zookeeperSessionTimeout;
    private String zookeeperRegistryPath;
    private String zookeeperNameSpace;

    public String getSerializerAlgorithm() {
        return serializerAlgorithm;
    }

    public void setSerializerAlgorithm(String serializerAlgorithm) {
        this.serializerAlgorithm = serializerAlgorithm;
    }

    public String getZookeeperAddress() {
        return zookeeperAddress;
    }

    public void setZookeeperAddress(String zookeeperAddress) {
        this.zookeeperAddress = zookeeperAddress;
    }

    public String getZookeeperSessionTimeout() {
        return zookeeperSessionTimeout;
    }

    public void setZookeeperSessionTimeout(String zookeeperSessionTimeout) {
        this.zookeeperSessionTimeout = zookeeperSessionTimeout;
    }

    public String getZookeeperRegistryPath() {
        return zookeeperRegistryPath;
    }

    public void setZookeeperRegistryPath(String zookeeperRegistryPath) {
        this.zookeeperRegistryPath = zookeeperRegistryPath;
    }

    public String getZookeeperNameSpace() {
        return zookeeperNameSpace;
    }

    public void setZookeeperNameSpace(String zookeeperNameSpace) {
        this.zookeeperNameSpace = zookeeperNameSpace;
    }
}
