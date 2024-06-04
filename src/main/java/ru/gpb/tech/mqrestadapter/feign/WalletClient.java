package ru.gpb.tech.mqrestadapter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceResponse;

@FeignClient(name = "walletClient", url = "${service.paths.wallet.url}")
public interface WalletClient {
    
    @PostMapping("/wallet/balance")
    ResponseEntity<GetWalletBalanceResponse> getBalance(GetWalletBalanceRequest request);
}
