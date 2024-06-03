package ru.gpb.tech.mqrestadapter.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.gpb.tech.mqrestadapter.config.FeatureToggleConfig;
import ru.gpb.tech.mqrestadapter.feign.WalletClient;
import ru.gpb.tech.mqrestadapter.mapper.WalletBalanceMapper;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.service.MqPublisher;
import ru.gpbtech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpbtech.mqrestadapter.model.GetWalletBalanceResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdapterServiceImplTest {
    
    @Mock
    private WalletClient walletClient;
    
    @Mock
    private WalletBalanceMapper mapper;
    
    @Mock
    private MqPublisher publisher;
    
    @Mock
    private FeatureToggleConfig featureToggleConfig;
    
    @InjectMocks
    private AdapterServiceImpl adapterService;
    
    @Test
    void testProcessRequest_withOldLogic() {
        ClientRequest request = new ClientRequest();
        OffsetDateTime dateTo = OffsetDateTime.now();
        request.setDateTo(dateTo);
        
        when(featureToggleConfig.isWalletIntegrationEnabled()).thenReturn(false);
        
        adapterService.processRequest(request);
        
        ArgumentCaptor<ClientResponse> responseCaptor = ArgumentCaptor.forClass(ClientResponse.class);
        verify(publisher).sendSuccess(responseCaptor.capture());
        ClientResponse capturedResponse = responseCaptor.getValue();
        
        assertEquals(dateTo, capturedResponse.getLastUpdated());
        assertEquals(BigDecimal.ONE, capturedResponse.getBalance());
        assertEquals("RUB", capturedResponse.getCurrency());
    }
    
    @Test
    void testProcessRequest_withNewLogic_success() {
        ClientRequest request = new ClientRequest();
        GetWalletBalanceRequest walletRequest = new GetWalletBalanceRequest();
        GetWalletBalanceResponse walletResponse = new GetWalletBalanceResponse();
        ClientResponse clientResponse = new ClientResponse();
        
        when(featureToggleConfig.isWalletIntegrationEnabled()).thenReturn(true);
        when(mapper.toWalletRequest(request)).thenReturn(walletRequest);
        when(walletClient.getBalance(walletRequest)).thenReturn(new ResponseEntity<>(walletResponse, HttpStatus.OK));
        when(mapper.toClientResponse(walletResponse)).thenReturn(clientResponse);
        
        adapterService.processRequest(request);
        
        verify(publisher).sendSuccess(clientResponse);
    }
    
    @Test
    void testProcessRequest_withNewLogic_failure() {
        ClientRequest request = new ClientRequest();
        GetWalletBalanceRequest walletRequest = new GetWalletBalanceRequest();
        
        when(featureToggleConfig.isWalletIntegrationEnabled()).thenReturn(true);
        when(mapper.toWalletRequest(request)).thenReturn(walletRequest);
        when(walletClient.getBalance(walletRequest)).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        
        adapterService.processRequest(request);
        
        verify(publisher).sendError("При обработке запроса произошла ошибка");
    }
}