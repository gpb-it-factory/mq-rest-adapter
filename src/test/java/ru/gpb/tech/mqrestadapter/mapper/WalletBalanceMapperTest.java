package ru.gpb.tech.mqrestadapter.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class WalletBalanceMapperTest {
    private final WalletBalanceMapper mapper = Mappers.getMapper(WalletBalanceMapper.class);
    
    @Test
    void toWalletRequest() {
        UUID clientId = UUID.randomUUID();
        OffsetDateTime dateFrom = OffsetDateTime.now();
        OffsetDateTime dateTo = OffsetDateTime.now().plusDays(1);
        
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setClientId(clientId);
        clientRequest.setDateFrom(dateFrom);
        clientRequest.setDateTo(dateTo);
        
        GetWalletBalanceRequest walletRequest = mapper.toWalletRequest(clientRequest);
        
        assertThat(walletRequest).isNotNull();
        assertThat(walletRequest.getClientId()).isEqualTo(clientId);
        assertThat(walletRequest.getDateFrom()).isEqualTo(dateFrom);
        assertThat(walletRequest.getDateTo()).isEqualTo(dateTo);
    }
    
    @Test
    void toClientResponse() {
        BigDecimal balance = BigDecimal.valueOf(100.00);
        String currency = "RUB";
        OffsetDateTime lastUpdated = OffsetDateTime.now();
        
        GetWalletBalanceResponse walletResponse = new GetWalletBalanceResponse();
        walletResponse.setBalance(balance);
        walletResponse.setCurrency(currency);
        walletResponse.setLastUpdated(lastUpdated);
        
        ClientResponse clientResponse = mapper.toClientResponse(walletResponse);
        
        assertThat(clientResponse).isNotNull();
        assertThat(clientResponse.getBalance()).isEqualTo(balance);
        assertThat(clientResponse.getCurrency()).isEqualTo(currency);
        assertThat(clientResponse.getLastUpdated()).isEqualTo(lastUpdated);
    }
    
}