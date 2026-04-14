package com.osmanli.banking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private double amount;

}
