package com.osmanli.banking.services;

import com.osmanli.banking.dto.TransferRequest;
import com.osmanli.banking.entity.Account;
import com.osmanli.banking.entity.Transaction;
import com.osmanli.banking.entity.User;
import com.osmanli.banking.repository.AccountRepository;
import com.osmanli.banking.repository.UserRepository;

import com.osmanli.banking.repository.TransactionRepository;
import java.time.LocalDateTime;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account createAccount(Account account, Long userId) {
       /* if (account.getAccountNumber() == null || account.getAccountNumber().isBlank()) {
            throw new RuntimeException("Account number must not be empty");
        }*/

/*
        if (accountRepository.findByAccountNumber(account.getAccountNumber()).isPresent()) {
            throw new RuntimeException("Account number already exists");
        }
*/

        if (account.getBalance() < 0) {
            throw new RuntimeException("Balance cannot be negative");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);

        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        String number;

        do {
            long randomNumber = (long) (Math.random() * 900000000L) + 100000000L;
            number = "ACC" + randomNumber;
        } while (accountRepository.findByAccountNumber(number).isPresent());

        return number;
    }

    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account deposit(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (amount <= 0) {
            throw new RuntimeException("Deposit amount must be greater than 0");
        }

        account.setBalance(account.getBalance() + amount);
        Account savedAccount= accountRepository.save(account);

        Transaction transaction=Transaction.builder()
                .type("DEPOSIT")
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .toAccount(savedAccount)
                .build();

        transactionRepository.save(transaction);
        return savedAccount;


    }

    public Account withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (amount <= 0) {
            throw new RuntimeException("Withdraw amount must be greater than 0");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        Account savedAccount= accountRepository.save(account);

        Transaction transaction=Transaction.builder()
                .type("WITHDRAW")
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .fromAccount(savedAccount)
                .build();
        transactionRepository.save(transaction);
        return savedAccount;
    }

    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.delete(account);
    }

    @Transactional
    public void transfer(TransferRequest request) {
        if(request.getAmount()<=0){
            throw new RuntimeException ("Amount must be greater than 0");
        }

        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccount())
                .orElseThrow(()-> new RuntimeException("Account not found"));

        Account toAccount= accountRepository.findByAccountNumber(request.getToAccount())
                .orElseThrow(()->new RuntimeException("Account not found"));

        if(fromAccount.getBalance()<=request.getAmount()){
            throw new RuntimeException ("Insufficient balance");
        }
        /*if(toAccount.getBalance()<=request.getAmount()){
            throw new RuntimeException ("Insufficient balance");
        }*/

        fromAccount.setBalance(fromAccount.getBalance()- request.getAmount());
        toAccount.setBalance(toAccount.getBalance()+request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction=Transaction.builder()
                .type("TRANSFER")
                .amount(request.getAmount())
                .createdAt(LocalDateTime.now())
                .toAccount(toAccount)
                .fromAccount(fromAccount)
                .build();

        transactionRepository.save(transaction);
    }


}