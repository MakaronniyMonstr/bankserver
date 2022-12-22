package com.vesko.balance.service;

import com.vesko.balance.dto.BalanceDto;

import java.util.List;

public interface BalanceService {
    BalanceDto save(BalanceDto account);

    BalanceDto getBalanceByUserId(Long id);

    List<BalanceDto> getAvailableIds();

    void changeBalanceByUserId(Long id, Long amount);
}
