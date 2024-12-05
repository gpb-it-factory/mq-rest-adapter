package ru.gpb.tech.mqrestadapter.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gpb.tech.mqrestadapter.config.FeatureToggleConfig;
import ru.gpb.tech.mqrestadapter.mapper.WalletBalanceMapper;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.service.MqPublisher;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceResponse;
import ru.gpb.tech.mqrestadapter.service.WalletService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdapterServiceImplTest {

    @Mock
    private WalletService walletService;

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
        when(walletService.getBalance(walletRequest)).thenReturn(Optional.of(new GetWalletBalanceResponse()));
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
        when(walletService.getBalance(walletRequest)).thenReturn(Optional.empty());

        adapterService.processRequest(request);

        verify(publisher).sendError("При обработке запроса произошла ошибка");
    }
}