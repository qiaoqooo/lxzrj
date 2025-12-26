package org.example.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 启动成功提示
 * 
 * @author code-generator
 */
@Component
public class StartupRunner implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8081}")
    private String port;

    @Value("${spring.application.name:code-generator}")
    private String applicationName;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║        " + applicationName.toUpperCase() + " 启动成功！                ║");
        System.out.println("║                                                              ║");
        System.out.println("║  服务地址: http://localhost:" + port + "                              ║");
        System.out.println("║  服务名称: " + applicationName + "                            ║");
        System.out.println("║                                                              ║");
        System.out.println("║  API接口:                                                      ║");
        System.out.println("║    POST http://localhost:" + port + "/api/generator/generate          ║");
        System.out.println("║    参数: moduleName, tableNames                              ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}

