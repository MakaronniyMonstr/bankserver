package com.vesko.balance.mapper;

import com.vesko.balance.dto.BalanceDto;
import com.vesko.balance.entity.Balance;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BalanceMapper {
    Balance toEntity(BalanceDto balanceDto);

    BalanceDto toDto(Balance balance);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Balance partialUpdate(BalanceDto balanceDto, @MappingTarget Balance balance);
}