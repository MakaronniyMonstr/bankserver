package com.vesko.balance.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.vesko.balance.view.OutputViews;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BalanceDto implements Serializable {
    @JsonView({OutputViews.Id.class})
    private final Long id;
    @JsonView({OutputViews.Balance.class})
    private final Long rubles;
}