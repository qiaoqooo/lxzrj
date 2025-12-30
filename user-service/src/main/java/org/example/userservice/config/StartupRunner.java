package org.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 启动成功提示
 * 
 * @author 小熊敲敲
 */
@Component
public class StartupRunner implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String port;

    @Value("${spring.application.name:user-service}")
    private String applicationName;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║            " + applicationName.toUpperCase() + " 启动成功！                    ║");
        System.out.println("║                                                              ║");
        System.out.println("║  服务地址: http://192.168.1.111:" + port + "                              ║");
        System.out.println("║  服务名称: " + applicationName + "                                    ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}

