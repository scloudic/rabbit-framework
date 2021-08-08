package com.scloudic.rabbitframework.core.springboot.configure;

import com.scloudic.rabbitframework.core.notification.NotificationServerManager;
import com.scloudic.rabbitframework.core.utils.CommonResponseUrl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * commons初始初始化启动类
 *
 * @since 3.3.1
 */
@Configuration
@EnableConfigurationProperties(RabbitCommonsProperties.class)
public class RabbitCommonsAutoConfiguration {
    private final RabbitCommonsProperties rabbitCommonsProperties;

    public RabbitCommonsAutoConfiguration(RabbitCommonsProperties rabbitCommonsProperties) {
        this.rabbitCommonsProperties = rabbitCommonsProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonResponseUrl commonResponseUrl() {
        CommonResponseUrl commonResponseUrl = new CommonResponseUrl();
        commonResponseUrl.setFrontBlack(rabbitCommonsProperties.isFrontBlack());
        commonResponseUrl.setLoginUrl(rabbitCommonsProperties.getLoginUrl());
        commonResponseUrl.setOtherError(rabbitCommonsProperties.getOtherError());
        commonResponseUrl.setSys404ErrorUrl(rabbitCommonsProperties.getSys404ErrorUrl());
        commonResponseUrl.setSys405ErrorUrl(rabbitCommonsProperties.getSys405ErrorUrl());
        commonResponseUrl.setSys500ErrorUrl(rabbitCommonsProperties.getSys500ErrorUrl());
        commonResponseUrl.setUnauthorizedUrl(rabbitCommonsProperties.getUnauthorizedUrl());
        commonResponseUrl.setPage404(rabbitCommonsProperties.isPage404());
        return commonResponseUrl;
    }

    @Bean(destroyMethod = "release")
    @ConditionalOnMissingBean
    public NotificationServerManager notificationServerManager() {
        return new NotificationServerManager();
    }
}
