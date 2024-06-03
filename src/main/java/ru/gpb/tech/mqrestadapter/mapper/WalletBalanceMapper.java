package ru.gpb.tech.mqrestadapter.mapper;

import org.mapstruct.Mapper;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpbtech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpbtech.mqrestadapter.model.GetWalletBalanceResponse;

@Mapper(componentModel = "spring")
public interface WalletBalanceMapper {
    
    GetWalletBalanceRequest toWalletRequest(ClientRequest clientRequest);
    
    ClientResponse toClientResponse(GetWalletBalanceResponse clientRequest);
}
