package com.microservicio_login.levelup_MSlogin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValidationResponse {

    private boolean valid;
    private String username;
    private String nombre;
    private Long id;
    private String roles;
}
