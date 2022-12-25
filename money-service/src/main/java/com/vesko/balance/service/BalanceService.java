package com.vesko.balance.service;

import com.vesko.balance.dto.BalanceDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BalanceService {
    BalanceDto create();

    BalanceDto getBalanceByUserId(Long id);

    List<BalanceDto> getAvailableIds(Pageable pageable);

    BalanceDto changeBalanceByUserId(Long id, Long amount);
}
