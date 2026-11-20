package com.microservicio_login.levelup_MSlogin.service;

import com.microservicio_login.levelup_MSlogin.dto.AuthRequest;
import com.microservicio_login.levelup_MSlogin.dto.AuthResponse;
import com.microservicio_login.levelup_MSlogin.dto.RegistroRequest;
import com.microservicio_login.levelup_MSlogin.dto.TokenValidationResponse;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse registrar(RegistroRequest registroRequest);

    AuthResponse getPerfil(String correo);

    TokenValidationResponse validarToken(String token);
}
