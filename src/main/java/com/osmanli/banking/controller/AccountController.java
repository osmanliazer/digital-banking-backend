package com.osmanli.banking.controller;

import com.osmanli.banking.dto.AccountResponse;
import com.osmanli.banking.dto.TransactionResponse;
import com.osmanli.banking.dto.TransferRequest;
import com.osmanli.banking.entity.Account;
import com.osmanli.banking.entity.Transaction;
import com.osmanli.banking.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService service;
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/user/{userId}")
    public AccountResponse createAccount(@RequestBody Account account,@Valid @PathVariable Long userId) {
        return service.createAccount(account, userId);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request){
        service.transfer(request);
        return "success";
    }

    @GetMapping("/{Id}")
    public AccountResponse getAccount(@PathVariable Long Id) {
        return service.getById(Id);
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return service.getAll();
    }

    @PutMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable Long id, @RequestBody double amount) {
        return service.deposit(id,amount);
    }

    @PutMapping("{id}/withdraw")
    public AccountResponse withdraw(@PathVariable Long id, @RequestBody double amount ){
        return service.withdraw(id, amount);
    }

        @DeleteMapping("/{id}")
    public String  delete(@PathVariable Long id){
        service.delete(id);
        return "Account deleted successfully";
        }


        @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getTransactions(@PathVariable Long id){
        return service.getAccountTransactions(id);
    }
}
