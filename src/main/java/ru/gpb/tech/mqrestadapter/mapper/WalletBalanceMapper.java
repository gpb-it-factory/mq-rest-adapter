package ru.gpb.tech.mqrestadapter.mapper;

import org.mapstruct.Mapper;
import ru.gpb.tech.mqrestadapter.model.ClientRequest;
import ru.gpb.tech.mqrestadapter.model.ClientResponse;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceRequest;
import ru.gpb.tech.mqrestadapter.model.GetWalletBalanceResponse;

/**
 * WalletBalanceMapper используется для маппинга объектов между различными уровнями приложения.
 * Этот интерфейс автоматически реализуется MapStruct во время компиляции.
 */
@Mapper(componentModel = "spring")
public interface WalletBalanceMapper {
    
    /**
     * Преобразует {@link ClientRequest} в {@link GetWalletBalanceRequest}.
     *
     * @param clientRequest запрос клиента
     * @return запрос для получения баланса кошелька
     */
    GetWalletBalanceRequest toWalletRequest(ClientRequest clientRequest);
    
    /**
     * Преобразует {@link GetWalletBalanceResponse} в {@link ClientResponse}.
     *
     * @param getWalletBalanceResponse ответ с балансом кошелька
     * @return ответ клиенту
     */
    ClientResponse toClientResponse(GetWalletBalanceResponse getWalletBalanceResponse);
}
