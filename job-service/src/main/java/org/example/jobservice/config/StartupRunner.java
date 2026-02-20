package org.example.jobservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 启动成功提示（与 user-service 风格保持一致）
 */
@Component
public class StartupRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    @Value("${server.port:8083}")
    private String port;

    @Value("${spring.application.name:job-service}")
    private String applicationName;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("");
        logger.info("╔══════════════════════════════════════════════════════════════╗");
        logger.info("║                                                              ║");
        logger.info("║            {} 启动成功！                                     ║", applicationName.toUpperCase());
        logger.info("║                                                              ║");
        logger.info("║  服务地址: http://localhost:{}                               ║", port);
        logger.info("║  服务名称: {}                                                ║", applicationName);
        logger.info("║                                                              ║");
        logger.info("╚══════════════════════════════════════════════════════════════╝");
        logger.info("");
    }
}






















