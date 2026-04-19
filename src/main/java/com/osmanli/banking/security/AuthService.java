package com.osmanli.banking.security;

import com.osmanli.banking.dto.AuthRequest;
import com.osmanli.banking.dto.AuthResponse;
import com.osmanli.banking.entity.User;
import com.osmanli.banking.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request){
        User user =userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Wrong password");
        }
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
