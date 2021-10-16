package com.scloudic.rabbitframework.redisson.springboot.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RabbitRedissonProperties.RABBIT_REDISSON_PREFIX)
public class RabbitRedissonProperties {
    public static final String RABBIT_REDISSON_PREFIX = "rabbit.redisson";
    private boolean openStatus = true;

    public boolean isOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(boolean openStatus) {
        this.openStatus = openStatus;
    }
}
