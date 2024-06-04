package ru.gpb.tech.mqrestadapter.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Класс представляет запрос клиента для получения баланса кошелька.
 */
@Data
public class ClientRequest {
    /**
     * Идентификатор клиента.
     */
    private UUID clientId;
    
    /**
     * Начальная дата запроса.
     */
    private OffsetDateTime dateFrom;
    
    /**
     * Конечная дата запроса.
     */
    private OffsetDateTime dateTo;
}
