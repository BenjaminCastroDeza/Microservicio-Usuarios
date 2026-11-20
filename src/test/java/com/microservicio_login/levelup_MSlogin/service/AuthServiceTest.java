package com.microservicio_login.levelup_MSlogin.service;

import com.microservicio_login.levelup_MSlogin.dto.AuthRequest;
import com.microservicio_login.levelup_MSlogin.dto.AuthResponse;
import com.microservicio_login.levelup_MSlogin.dto.RegistroRequest;
import com.microservicio_login.levelup_MSlogin.exception.BadRequestException;
import com.microservicio_login.levelup_MSlogin.model.Role;
import com.microservicio_login.levelup_MSlogin.model.Usuario;
import com.microservicio_login.levelup_MSlogin.repository.UsuarioRepository;
import com.microservicio_login.levelup_MSlogin.security.JwtTokenProvider;
import com.microservicio_login.levelup_MSlogin.security.UserPrincipal;
import com.microservicio_login.levelup_MSlogin.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private Usuario usuario;
    private RegistroRequest registroRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nombre("Benjamin Castro")
                .correo("benjamin@mail.com")
                .contrasena("encodedPassword")
                .telefono("+56 9 1234 5678")
                .region("Region de Valparaiso")
                .comuna("Viña del Mar")
                .rol(Role.ROLE_CLIENTE)
                .build();

        registroRequest = RegistroRequest.builder()
                .nombre("Benjamin Castro")
                .correo("benjamin@mail.com")
                .contrasena("Admin123!")
                .telefono("+56 9 1234 5678")
                .region("Region de Valparaiso")
                .comuna("Viña del Mar")
                .build();

        authRequest = AuthRequest.builder()
                .correo("benjamin@mail.com")
                .contrasena("Admin123!")
                .build();
    }

    @Test
    @DisplayName("Debe registrar un nuevo usuario exitosamente")
    void testRegistrarExitoso() {
        when(usuarioRepository.existsByCorreo("benjamin@mail.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("jwt.token.mock");

        AuthResponse response = authService.registrar(registroRequest);

        assertNotNull(response);
        assertEquals("jwt.token.mock", response.getToken());
        assertEquals("benjamin@mail.com", response.getCorreo());
        assertEquals("ROLE_CLIENTE", response.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el correo ya existe al registrar")
    void testRegistrarCorreoExistente() {
        when(usuarioRepository.existsByCorreo("benjamin@mail.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.registrar(registroRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe iniciar sesión exitosamente y retornar token JWT")
    void testLoginExitoso() {
        Authentication auth = mock(Authentication.class);
        UserPrincipal principal = new UserPrincipal(1L, "Benjamin Castro", "benjamin@mail.com", "pass", Collections.emptyList());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(principal);
        when(tokenProvider.generateToken(auth)).thenReturn("jwt.token.mock");
        when(usuarioRepository.findByCorreo("benjamin@mail.com")).thenReturn(Optional.of(usuario));

        AuthResponse response = authService.login(authRequest);

        assertNotNull(response);
        assertEquals("jwt.token.mock", response.getToken());
        assertEquals("benjamin@mail.com", response.getCorreo());
    }
}
