package com.osmanli.banking.controller;

import com.osmanli.banking.dto.TransferRequest;
import com.osmanli.banking.entity.Account;
import com.osmanli.banking.entity.Transaction;
import com.osmanli.banking.services.AccountService;
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
    public Account createAccount(@RequestBody Account account, @PathVariable Long userId) {
        return service.createAccount(account, userId);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request){
        service.transfer(request);
        return "success";
    }

    @GetMapping("/{Id}")
    public Account getAccount(@PathVariable Long Id) {
        return service.getById(Id);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return service.getAll();
    }

    @PutMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestBody double amount) {
        return service.deposit(id,amount);
    }

    @PutMapping("{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestBody double amount ){
        return service.withdraw(id, amount);
    }

        @DeleteMapping("/{id}")
    public String  delete(@PathVariable Long id){
        service.delete(id);
        return "Account deleted successfully";
        }


        @GetMapping("/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable Long id){
        return service.getAccountTransactions(id);
    }
}
