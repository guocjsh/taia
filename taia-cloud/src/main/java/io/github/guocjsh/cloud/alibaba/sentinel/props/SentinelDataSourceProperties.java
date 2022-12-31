package io.github.guocjsh.cloud.alibaba.sentinel.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {SentinelDataSourceProperties.class})
@ConfigurationProperties(prefix = "spring.cloud.taia.sentinel")
public class SentinelDataSourceProperties {

    /**
     * sentinel dataSource
     */
    private String dataSource;

    /**
     * nacos server ip
     */
    private String serverAddr;

    /**
     * nacos group
     */
    private String groupId;

    /**
     * nacos dataId
     */
    private String dataId;

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}