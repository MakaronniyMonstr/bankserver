package com.vesko.balance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "balance")
@Getter
@Setter
@ToString
public class Balance extends BaseEntity {
    @Column(name = "rubles")
    private Long rubles = 0L;
}
