package ru.gpb.tech.mqrestadapter.integration;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.service.MqPublisher;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 9999)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"features.walletIntegrationEnabled=true"})
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
    
    static void startContainer() {
        MQ_CONTAINER.start();
    }
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private MqPublisher mqPublisher;
    
    @Test
    void successExecution() {
        stubFor(post(urlMatching("/wallet/balance"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"balance\": 100.00, \"currency\": \"RUB\", \"lastUpdated\": \"2024-06-01T12:00:00Z\" }")));
        
        /*
         * stubFor(post(urlMatching("/wallet/balance")).willReturn(status(500))); - проверка fail запроса
         *
         * stubFor(post(urlMatching("/wallet/balance"))
         *         .willReturn(aResponse()
         *                 .withBodyFile("path/to/myfile.json"))); - чтение заглушки из файла
         */
        
        ClientRequest request = new ClientRequest();
        request.setClientId(UUID.randomUUID());
        request.setDateTo(OffsetDateTime.now().plusDays(1));
        request.setDateTo(OffsetDateTime.now());
        
        rabbitTemplate.convertAndSend("adapterQueue", request);
        
        ArgumentCaptor<ClientResponse> responseCaptor = ArgumentCaptor.forClass(ClientResponse.class);
        verify(mqPublisher, timeout(2000)).sendSuccess(responseCaptor.capture());
        ClientResponse capturedResponse = responseCaptor.getValue();
        
        assertThat(capturedResponse)
                .hasFieldOrPropertyWithValue("currency", "RUB");
    }
}
