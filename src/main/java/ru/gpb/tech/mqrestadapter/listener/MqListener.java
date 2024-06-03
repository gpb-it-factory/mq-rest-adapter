package ru.gpb.tech.mqrestadapter.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.service.AdapterService;

@Log4j2
@Component
public class MqListener {
    @Autowired
    private AdapterService adapterService;
    
    @RabbitListener(queues = "adapterQueue")
    public void receiveMessage(ClientRequest request) {
        log.info("Получено сообщение клиента для предоставления актуальной информации");
        adapterService.processRequest(request);
    }
}
