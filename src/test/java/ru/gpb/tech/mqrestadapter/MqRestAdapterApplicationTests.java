package ru.gpb.tech.mqrestadapter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 7777)
@Testcontainers
class MqRestAdapterApplicationTests {
    
    @Container
    private static final RabbitMQContainer MQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.10.7");
    
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", MQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", MQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", MQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", MQ_CONTAINER::getAdminPassword);
    }
    
    @BeforeAll
    static void startContainer() {
        MQ_CONTAINER.start();
    }
    
    @AfterAll
    static void stopContainer() {
        MQ_CONTAINER.stop();
    }
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Test
    void contextLoads() {
        stubFor(post(urlEqualTo("/wallet/balance"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"balance\": 100.00, \"currency\": \"USD\", \"lastUpdated\": \"2024-06-01T12:00:00Z\" }")));
        
        //rabbitTemplate.convertAndSend();
    }
    
}
