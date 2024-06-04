package ru.gpb.tech.mqrestadapter.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;

/**
 * Класс для имитации отправки данных клиенту
 */
@Log4j2
@Component
public class MqPublisher {
    
    /**
     * Имитация отправки успешного ответа клиенту
     *
     * @param clientResponse данные для передачи клиенту
     */
    public void sendSuccess(ClientResponse clientResponse) {
        log.info("Отправка успешного ответа {}", clientResponse);
    }
    
    /**
     * Имитация отправки клиенту ответа с ошибкой
     *
     * @param error данные об ошибке
     */
    public void sendError(String error) {
        log.info("Отправка ответа с ошибкой {}", error);
    }
}
