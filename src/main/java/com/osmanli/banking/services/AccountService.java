package com.osmanli.banking.services;
import com.osmanli.banking.dto.TransactionResponse;
import com.osmanli.banking.dto.TransferRequest;
import com.osmanli.banking.entity.Account;
import com.osmanli.banking.entity.Transaction;
import com.osmanli.banking.entity.User;
import com.osmanli.banking.exception.AccountNotFound;
import com.osmanli.banking.exception.InsufficientBalance;
import com.osmanli.banking.exception.UserNotFound;
import com.osmanli.banking.repository.AccountRepository;
import com.osmanli.banking.repository.UserRepository;
import com.osmanli.banking.dto.AccountResponse;
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

    public AccountResponse createAccount(Account account, Long userId) {
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
                .orElseThrow(() -> new UserNotFound("User not found"));

        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        Account savedAccount = accountRepository.save(account);
        return mapToAccountResponse(savedAccount);
    }

    private String generateAccountNumber() {
        String number;

        do {
            long randomNumber = (long) (Math.random() * 900000000L) + 100000000L;
            number = "ACC" + randomNumber;
        } while (accountRepository.findByAccountNumber(number).isPresent());

        return number;
    }

    public AccountResponse getById(Long id) {
        Account account= accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFound("Account not found"));
        return mapToAccountResponse(account);
    }

    public List<AccountResponse> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(this::mapToAccountResponse)
                .toList();
    }



    public List<TransactionResponse> getAccountTransactions(Long accountId) {
        Account account=accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound("Account not found"));
        return transactionRepository.findByFromAccountOrToAccount(account, account)
                .stream()
                .map(this::mapToTransactionResponse)
                .toList();

    }

    public AccountResponse deposit(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFound("Account not found"));

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
        return mapToAccountResponse(savedAccount);


    }

    public AccountResponse withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFound("Account not found"));

        if (amount <= 0) {
            throw new RuntimeException("Withdraw amount must be greater than 0");
        }

        if (account.getBalance() < amount) {
            throw new InsufficientBalance("Insufficient balance");
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
        return mapToAccountResponse(savedAccount);
    }

    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFound("Account not found"));

        accountRepository.delete(account);
    }

    @Transactional
    public void transfer(TransferRequest request) {
        if(request.getAmount()<=0){
            throw new RuntimeException ("Amount must be greater than 0");
        }

        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccount())
                .orElseThrow(()-> new AccountNotFound("Account not found"));

        Account toAccount= accountRepository.findByAccountNumber(request.getToAccount())
                .orElseThrow(()->new AccountNotFound("Account not found"));

        if(fromAccount.getBalance()<=request.getAmount()){
            throw new InsufficientBalance("Insufficient balance");
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


    private AccountResponse mapToAccountResponse(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .build();
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .fromAccountNumber(transaction.getFromAccount()!=null? transaction.getFromAccount().getAccountNumber():null)
                .toAccountNumber(transaction.getToAccount()!=null? transaction.getToAccount().getAccountNumber():null)
                .createdAt(transaction.getCreatedAt())
                .build();
    }


}