package com.osmanli.banking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder



public class UserResponse {
    private Long id;
    private String name;
    private String email;

}
