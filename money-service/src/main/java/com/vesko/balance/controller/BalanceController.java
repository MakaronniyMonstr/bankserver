package com.vesko.balance.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.vesko.balance.dto.BalanceDto;
import com.vesko.balance.service.BalanceService;
import com.vesko.balance.view.OutputViews;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping
    @JsonView({OutputViews.Id.class})
    public List<BalanceDto> getAvailableBalanceIds() {
        return balanceService.getAvailableIds();
    }

    @GetMapping("/{id}")
    @JsonView({OutputViews.Balance.class})
    public BalanceDto getBalanceById(@PathVariable Long id) {
        return balanceService.getBalanceByUserId(id);
    }

    @PostMapping("/{id}")
    public void addBalanceById(@PathVariable Long id, @RequestParam Long amount) {
        balanceService.changeBalanceByUserId(id, amount);
    }
}
