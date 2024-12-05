package ru.gpb.tech.mqrestadapter.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gpb.tech.mqrestadapter.feign.WalletClient;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceResponse;

import java.util.Optional;

/**
 * Сервис получения баланса кошелька
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletClient walletClient;

    /**
     * Получение баланса кошелька
     */
    @CircuitBreaker(name = "getBalanceCircuitBreaker", fallbackMethod = "getBalanceRecoverMethod")
    public Optional<GetWalletBalanceResponse> getBalance(GetWalletBalanceRequest request) {
        return Optional.of(walletClient.getBalance(request))
                .filter(response -> HttpStatus.OK.equals(response.getStatusCode()))
                .map(HttpEntity::getBody);
    }

    /**
     * Метод, вызываемый в случае недоступности сервиса баланса кошелька
     */
    private Optional<GetWalletBalanceResponse> getBalanceRecoverMethod(Exception ex) {
        log.warn("Сервис баланса кошелька недоступен, будет возвращено пустое значение", ex);

        return Optional.empty();
    }
}
