package org.example.common.config;

import org.example.common.handler.MyMetaObjectHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Common 模块自动配置类
 * 自动注册 MyMetaObjectHandler 等通用组件
 * 
 * @author common
 */
@Configuration
@ComponentScan(basePackages = "org.example.common")
public class CommonAutoConfiguration {

    /**
     * 注册 MyMetaObjectHandler
     * 如果已经存在，则不注册（允许子模块自定义）
     */
    @Bean
    @ConditionalOnMissingBean(MyMetaObjectHandler.class)
    public MyMetaObjectHandler myMetaObjectHandler() {
        return new MyMetaObjectHandler();
    }
}

