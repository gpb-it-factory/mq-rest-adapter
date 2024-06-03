package ru.gpb.tech.mqrestadapter.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ClientRequest {
    private UUID clientId;
    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;
}
