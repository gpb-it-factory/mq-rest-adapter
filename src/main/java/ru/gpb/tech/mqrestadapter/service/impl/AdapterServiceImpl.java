package ru.gpb.tech.mqrestadapter.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.service.AdapterService;
import ru.gpb.tech.mqrestadapter.service.MqPublisher;

import java.math.BigDecimal;

/**
 * Класс для обработки запросов клиентов
 */
@Service
@RequiredArgsConstructor
public class AdapterServiceImpl implements AdapterService {
    private final MqPublisher publisher;
    
    /**
     * Обрабатывает запрос клиента, создает ответ клиента и отправляет его через {@link MqPublisher}.
     *
     * @param request Объект {@link ClientRequest}, содержащий данные запроса клиента.
     *                Поля запроса включают конечную дату (dateTo), которая используется для установки
     *                последнего обновления в ответе клиента.
     */
    @Override
    public void processRequest(ClientRequest request) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setLastUpdated(request.getDateTo());
        clientResponse.setBalance(BigDecimal.ONE);
        clientResponse.setCurrency("RUB");
        
        publisher.sendSuccess(clientResponse);
    }
}
