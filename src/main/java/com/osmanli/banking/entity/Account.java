package com.osmanli.banking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name="accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    @PositiveOrZero(message="balance must not be negative")
    private double balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
