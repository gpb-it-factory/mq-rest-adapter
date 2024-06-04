package ru.gpb.tech.mqrestadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.gpb.tech.mqrestadapter.feign")
public class MqRestAdapterApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MqRestAdapterApplication.class, args);
    }
    
}
