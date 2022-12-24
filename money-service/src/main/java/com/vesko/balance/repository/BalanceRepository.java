package com.vesko.balance.repository;

import com.vesko.balance.entity.Balance;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = AvailableHints.HINT_SPEC_LOCK_TIMEOUT, value = "30000")})
    @Query("select b from Balance b where b.id = :id")
    Optional<Balance> findByIdWithWriteLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = AvailableHints.HINT_SPEC_LOCK_TIMEOUT, value = "30000")})
    @Query("select b from Balance b where b.id = :id")
    Optional<Balance> findByIdWithReadLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Balance> findAllBy();
}