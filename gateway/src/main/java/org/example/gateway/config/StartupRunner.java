package org.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 启动成功提示
 * 
 * @author gateway
 */
@Component
public class StartupRunner implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8888}")
    private String port;

    @Value("${spring.application.name:gateway}")
    private String applicationName;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║            " + applicationName.toUpperCase() + " 启动成功！                    ║");
        System.out.println("║                                                              ║");
        System.out.println("║  网关地址: http://localhost:" + port + "                              ║");
        System.out.println("║  服务名称: " + applicationName + "                                    ║");
        System.out.println("║                                                              ║");
        System.out.println("║  路由配置:                                                    ║");
        System.out.println("║    - 用户服务: http://localhost:" + port + "/api/user/**              ║");
        System.out.println("║    - 代码生成器: http://localhost:" + port + "/api/generator/**      ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}

