package ru.gpb.tech.mqrestadapter.service;

import ru.gpb.tech.mqrestadapter.model.ClientRequest;

public interface AdapterService {
    void processRequest(ClientRequest request);
}
