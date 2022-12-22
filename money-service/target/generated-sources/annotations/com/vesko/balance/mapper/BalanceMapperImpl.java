package com.vesko.balance.mapper;

import com.vesko.balance.dto.BalanceDto;
import com.vesko.balance.entity.Balance;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-22T00:08:24+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class BalanceMapperImpl implements BalanceMapper {

    @Override
    public Balance toEntity(BalanceDto balanceDto) {
        if ( balanceDto == null ) {
            return null;
        }

        Balance balance = new Balance();

        balance.setId( balanceDto.getId() );

        return balance;
    }

    @Override
    public BalanceDto toDto(Balance balance) {
        if ( balance == null ) {
            return null;
        }

        BalanceDto.BalanceDtoBuilder balanceDto = BalanceDto.builder();

        balanceDto.id( balance.getId() );

        return balanceDto.build();
    }

    @Override
    public Balance partialUpdate(BalanceDto balanceDto, Balance balance) {
        if ( balanceDto == null ) {
            return balance;
        }

        if ( balanceDto.getId() != null ) {
            balance.setId( balanceDto.getId() );
        }

        return balance;
    }
}
