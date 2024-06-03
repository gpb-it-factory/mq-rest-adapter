package ru.gpb.tech.mqrestadapter.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.service.MqPublisher;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdapterServiceImplTest {
    
    @Mock
    private MqPublisher publisher;
    
    @InjectMocks
    private AdapterServiceImpl clientService;
    
    @Test
    void testProcessRequest() {
        ClientRequest request = new ClientRequest();
        OffsetDateTime dateTo = OffsetDateTime.now();
        request.setDateTo(dateTo);
        
        clientService.processRequest(request);
        
        ArgumentCaptor<ClientResponse> responseCaptor = ArgumentCaptor.forClass(ClientResponse.class);
        verify(publisher).sendSuccess(responseCaptor.capture());
        ClientResponse capturedResponse = responseCaptor.getValue();
        
        assertEquals(dateTo, capturedResponse.getLastUpdated());
        assertEquals(BigDecimal.ONE, capturedResponse.getBalance());
        assertEquals("RUB", capturedResponse.getCurrency());
    }
}