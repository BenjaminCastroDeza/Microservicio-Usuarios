package com.microservicio_login.levelup_MSlogin.controller;

import com.microservicio_login.levelup_MSlogin.dto.AuthRequest;
import com.microservicio_login.levelup_MSlogin.dto.AuthResponse;
import com.microservicio_login.levelup_MSlogin.dto.RegistroRequest;
import com.microservicio_login.levelup_MSlogin.dto.TokenValidationResponse;
import com.microservicio_login.levelup_MSlogin.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro, inicio de sesión y gestión de JWT de usuarios")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con correo y contraseña, retornando un token JWT y datos de sesión.")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un cliente en la plataforma validando datos personales y contraseña.")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest registroRequest) {
        AuthResponse response = authService.registrar(registroRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener perfil del usuario autenticado", description = "Retorna la información del usuario autenticado a partir de su JWT.")
    public ResponseEntity<AuthResponse> getPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AuthResponse perfil = authService.getPerfil(userDetails.getUsername());
        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar Token JWT", description = "Verifica la validez de un token y retorna los claims asociados.")
    public ResponseEntity<TokenValidationResponse> validarToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.ok(TokenValidationResponse.builder().valid(false).build());
        }
        return ResponseEntity.ok(authService.validarToken(authHeader));
    }
}
