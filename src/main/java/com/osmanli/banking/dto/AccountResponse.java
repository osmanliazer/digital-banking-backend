package com.osmanli.banking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AccountResponse {
    private Long id;
    private String accountNumber;
    private double balance;
    private String userName;
    private Long userId;
}
