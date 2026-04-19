package com.osmanli.banking.services;
import com.osmanli.banking.dto.UserResponse;
import com.osmanli.banking.exception.UserNotFound;
import com.osmanli.banking.repository.UserRepository;
import com.osmanli.banking.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;

    }


    public UserResponse register(User user) {

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser= repository.save(user);
        return mapToUserResponse(savedUser);
    }

    public UserResponse getById(long id) {
        User user=repository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();


    }

    public UserResponse update(Long id, User updatedUser) {

        User existing = repository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));

        if (updatedUser.getName() != null) {
            existing.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            existing.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getEmail() != null) {
            Optional<User> userByEmail = repository.findByEmail(updatedUser.getEmail());

            if (userByEmail.isPresent() && !userByEmail.get().getId().equals(existing.getId())) {
                throw new RuntimeException("Email already in use");
            }

            existing.setEmail(updatedUser.getEmail());
        }

        User savedUser= repository.save(existing);
        return mapToUserResponse(savedUser);

    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found"));
        repository.delete(user);

    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
