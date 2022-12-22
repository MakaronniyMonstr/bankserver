package com.vesko.balance.repository;

import com.vesko.balance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    <T> Optional<T> findById(Long id, Class<T> type);

    @Modifying
    @Query("update Balance a set a.rubles = a.rubles + :amount where a.id = :id and a.rubles + :amount >= 0")
    int changeBalanceByUserId(Long id, Long amount);

    <T> List<T> findAllBy(Class<T> type);
}