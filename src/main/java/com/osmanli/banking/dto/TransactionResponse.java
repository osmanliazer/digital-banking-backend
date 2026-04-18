package com.osmanli.banking.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionResponse {
    private Long id;
    private String type;
    private double amount;
    private String fromAccountNumber;
    private String toAccountNumber;
    private LocalDateTime createdAt;
}
