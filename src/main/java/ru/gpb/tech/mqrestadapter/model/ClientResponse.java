package ru.gpb.tech.mqrestadapter.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Класс представляет ответ клиенту с информацией о балансе кошелька.
 */
@Data
public class ClientResponse {
    /**
     * Баланс кошелька.
     */
    private BigDecimal balance;
    
    /**
     * Валюта баланса.
     */
    private String currency;
    
    /**
     * Время последнего обновления баланса.
     */
    private OffsetDateTime lastUpdated;
}
