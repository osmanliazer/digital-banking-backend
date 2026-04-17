package com.osmanli.banking.repository;

import com.osmanli.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
