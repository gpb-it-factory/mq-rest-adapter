package ru.gpb.tech.mqrestadapter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import ru.gpbtech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpbtech.mqrestadapter.model.GetWalletBalanceResponse;

@FeignClient(name = "walletClient", url = "http://localhost:8080")
public interface WalletClient {
    
    @PostMapping("/wallet/balance")
    ResponseEntity<GetWalletBalanceResponse> getBalance(GetWalletBalanceRequest request);
}
