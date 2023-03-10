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
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository repository;
    private final BalanceMapper mapper;

    @Transactional
    @Override
    public BalanceDto create() {
        var entity = new Balance();
        return mapper.toDto(repository.save(entity));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Cacheable(value = "balances", key = "#id")
    @Override
    public BalanceDto getBalanceByUserId(Long id) {
        notNull(id, "Balance id must not be null");

        var balance = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return mapper.toDto(balance);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    @Override
    public List<BalanceDto> getAvailableIds(Pageable pageable) {
        return repository.findAll(pageable)
                .stream()
                .map(balanceIdInfo -> BalanceDto.builder()
                        .id(balanceIdInfo.getId())
                        .build())
                .toList();
    }

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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CachePut(value = "balances", key = "#id")
    @Override
    public BalanceDto changeBalanceByUserId(Long id, Long amount) {
        notNull(id, "Balance id must not be null");
        notNull(amount, "Amount must not be null");

        var balance = repository.findByIdWithWriteLock(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        var rubles = balance.getRubles();
        if (rubles + amount < 0) {
            throw new ResourceCannotBeModifiedException("The balance cannot become negative");
        }
        balance.setRubles(rubles + amount);

        return mapper.toDto(balance);
    }
}
