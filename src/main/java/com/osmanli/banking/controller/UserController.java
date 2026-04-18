package com.osmanli.banking.controller;

import com.osmanli.banking.dto.UserResponse;
import com.osmanli.banking.entity.User;
import com.osmanli.banking.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponse register(@Valid @RequestBody User user) {
        return service.register(user);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable long id, @Valid @RequestBody User updatedUser) {
        return service.update(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        service.delete(id);
        return "User deleted successfully";
    }
}