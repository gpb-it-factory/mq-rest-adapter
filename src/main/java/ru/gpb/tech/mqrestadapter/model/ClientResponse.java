package ru.gpb.tech.mqrestadapter.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class ClientResponse {
    private BigDecimal balance;
    private String currency;
    private OffsetDateTime lastUpdated;
}
