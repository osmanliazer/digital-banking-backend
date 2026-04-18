package com.osmanli.banking.exception;

import com.osmanli.banking.entity.Account;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;




@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> handleUserNotFound(UserNotFound userNotFound){
        Map<String,Object> error= new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 404);
        error.put("error", "User not found");
        error.put("message",userNotFound.getMessage());
        return error;
    }

    @ExceptionHandler(AccountNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> handleAccountNotFound(AccountNotFound accountNotFound){
        Map<String, Object> error= new HashMap<>();
        error.put("TimesStamp", LocalDateTime.now());
        error.put("status", 404);
        error.put("error","Account not found");
        error.put("message",accountNotFound.getMessage());
        return error;
    }
    @ExceptionHandler(InsufficientBalance.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> handleInsufficientBalance(InsufficientBalance insufficientBalance){
        Map<String, Object> error= new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("Status",404);
        error.put("error","Insufficient Balance");
        error.put("message",insufficientBalance.getMessage());
        return error;
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleRuntimeException(RuntimeException ex){
        Map<String, Object> error= new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("error", "Bad Request");
        error.put("error", ex.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> validationErrors.put(err.getField(), err.getDefaultMessage()));

        error.put("errors", validationErrors);
        return error;
    }

}
