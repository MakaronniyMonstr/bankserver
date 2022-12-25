package com.vesko.balance.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vesko.aop.statistics.annotation.Statistic;
import com.vesko.balance.dto.BalanceDto;
import com.vesko.balance.service.BalanceService;
import com.vesko.balance.view.OutputViews;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping
    @JsonView({OutputViews.Id.class})
    public List<BalanceDto> getAvailableBalanceIds(@PageableDefault Pageable pageable) {
        return balanceService.getAvailableIds(pageable);
    }

    @Statistic(region = "read")
    @GetMapping("/{id}")
    @JsonView({OutputViews.Balance.class})
    public BalanceDto getBalanceById(@PathVariable Long id) {
        return balanceService.getBalanceByUserId(id);
    }

    @Statistic(region = "write")
    @JsonView({OutputViews.Balance.class})
    @PostMapping("/{id}")
    public BalanceDto addBalanceById(@PathVariable Long id, @RequestParam Long amount) {
        return balanceService.changeBalanceByUserId(id, amount);
    }

    @PostMapping
    public BalanceDto createBalance() {
        return balanceService.create();
    }
}
