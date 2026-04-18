package com.osmanli.banking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message= "Name must not be empty")
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank(message="Email must not be empty")
    @Email(message="Invalid email format")
    private String email;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Password must not be empty")
    @Size(min=6, message= "passwor must be at least 6 character")
    private String password;

}
