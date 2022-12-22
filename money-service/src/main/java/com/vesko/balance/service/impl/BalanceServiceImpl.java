package com.vesko.balance.service.impl;

import com.vesko.application.exception.ResourceCannotBeModifiedException;
import com.vesko.application.exception.ResourceNotFoundException;
import com.vesko.balance.dto.BalanceDto;
import com.vesko.balance.entity.Balance;
import com.vesko.balance.mapper.BalanceMapper;
import com.vesko.balance.projection.BalanceIdInfo;
import com.vesko.balance.projection.BalanceRublesInfo;
import com.vesko.balance.repository.BalanceRepository;
import com.vesko.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository repository;
    private final BalanceMapper mapper;

    @Override
    public BalanceDto create() {
        var entity = new Balance();
        return mapper.toDto(repository.save(entity));
    }

    @Cacheable(value = "balances", key = "#id")
    @Override
    public BalanceDto getBalanceByUserId(Long id) {
        assert id != null;

        var accountIdInfo = repository.findById(id, BalanceRublesInfo.class)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return BalanceDto.builder()
                .rubles(accountIdInfo.getRubles())
                .build();
    }

    @Override
    public List<BalanceDto> getAvailableIds() {
        return repository.findAllBy(BalanceIdInfo.class)
                .stream()
                .map(balanceIdInfo -> BalanceDto.builder()
                        .id(balanceIdInfo.getId())
                        .build())
                .toList();
    }

    @CacheEvict(value = "balances", key = "#id")
    @Override
    public void changeBalanceByUserId(Long id, Long amount) {
        assert id != null;
        assert amount != null;

        int changedCount = repository.changeBalanceByUserId(id, amount);

        assert changedCount == 2 || changedCount < 0;

        if (changedCount == 0) {
            throw new ResourceCannotBeModifiedException("The balance cannot become negative");
        }
    }
}
