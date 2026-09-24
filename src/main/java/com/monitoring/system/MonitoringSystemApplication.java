package com.monitoring.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonitoringSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                MonitoringSystemApplication.class,
                args);
    }
}