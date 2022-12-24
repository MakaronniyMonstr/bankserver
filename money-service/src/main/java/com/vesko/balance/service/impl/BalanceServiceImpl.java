package com.vesko.balance.service.impl;

import com.vesko.application.exception.ResourceCannotBeModifiedException;
import com.vesko.application.exception.ResourceNotFoundException;
import com.vesko.balance.dto.BalanceDto;
import com.vesko.balance.entity.Balance;
import com.vesko.balance.mapper.BalanceMapper;
import com.vesko.balance.repository.BalanceRepository;
import com.vesko.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Retryable(
        retryFor = {
                LockAcquisitionException.class
        },
        maxAttempts = 10,
        backoff = @Backoff(
                delay = 10,
                multiplier = 2,
                random = true
        )
)
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

        var balance = repository.findByIdWithReadLock(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return mapper.toDto(balance);
    }

    @Override
    public List<BalanceDto> getAvailableIds() {
        return repository.findAllBy()
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

        var balance = repository.findByIdWithWriteLock(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        var rubles = balance.getRubles();
        if (rubles + amount < 0) {
            throw new ResourceCannotBeModifiedException("The balance cannot become negative");
        }

        balance.setRubles(rubles + amount);
    }
}
