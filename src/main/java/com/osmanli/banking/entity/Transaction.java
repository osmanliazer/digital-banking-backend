package com.osmanli.banking.entity;

import jakarta.persistence.*;

import jakarta.persistence.Entity;
import lombok.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;


@Entity
@Table(name="transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String type;
    private double amount;
    public LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="from_account_id")
    private Account fromAccount;
    @ManyToOne
    @JoinColumn(name="to_account_id")
    private Account toAccount;


}
