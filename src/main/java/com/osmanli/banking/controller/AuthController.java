package com.osmanli.banking.controller;
import com.osmanli.banking.dto.AuthRequest;
import com.osmanli.banking.dto.AuthResponse;
import com.osmanli.banking.entity.Account;
import com.osmanli.banking.security.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request)
    {
        return service.login(request);
    }
}
