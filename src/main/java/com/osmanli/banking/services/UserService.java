package com.osmanli.banking.services;

import com.osmanli.banking.exception.UserNotFound;
import com.osmanli.banking.repository.UserRepository;
import com.osmanli.banking.entity.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;


    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public User register(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email must not be empty");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if(user.getName()==null || user.getName().isEmpty()){
            throw new RuntimeException("Name must not be empty");
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        return repository.save(user);
    }

    public User getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User update(Long id, User updatedUser) {

        User existing = repository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));

        if (updatedUser.getName() != null) {
            existing.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null) {
            existing.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getEmail() != null) {
            Optional<User> userByEmail = repository.findByEmail(updatedUser.getEmail());

            if (userByEmail.isPresent() && !userByEmail.get().getId().equals(existing.getId())) {
                throw new RuntimeException("Email already in use");
            }

            existing.setEmail(updatedUser.getEmail());
        }

        return repository.save(existing);

    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));
        repository.delete(user);

    }

}
