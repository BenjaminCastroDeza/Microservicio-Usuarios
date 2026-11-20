package com.microservicio_login.levelup_MSlogin.service.impl;

import com.microservicio_login.levelup_MSlogin.dto.AuthRequest;
import com.microservicio_login.levelup_MSlogin.dto.AuthResponse;
import com.microservicio_login.levelup_MSlogin.dto.RegistroRequest;
import com.microservicio_login.levelup_MSlogin.dto.TokenValidationResponse;
import com.microservicio_login.levelup_MSlogin.exception.BadRequestException;
import com.microservicio_login.levelup_MSlogin.exception.ResourceNotFoundException;
import com.microservicio_login.levelup_MSlogin.model.Role;
import com.microservicio_login.levelup_MSlogin.model.Usuario;
import com.microservicio_login.levelup_MSlogin.repository.UsuarioRepository;
import com.microservicio_login.levelup_MSlogin.security.JwtTokenProvider;
import com.microservicio_login.levelup_MSlogin.security.UserPrincipal;
import com.microservicio_login.levelup_MSlogin.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        String correoLimpio = authRequest.getCorreo().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        correoLimpio,
                        authRequest.getContrasena()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByCorreo(userPrincipal.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", userPrincipal.getCorreo()));

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        String correoLimpio = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correoLimpio)) {
            throw new BadRequestException("Este correo ya se encuentra registrado.");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .correo(correoLimpio)
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .telefono(request.getTelefono() != null ? request.getTelefono().trim() : null)
                .region(request.getRegion().trim())
                .comuna(request.getComuna().trim())
                .rol(Role.ROLE_CLIENTE)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correoLimpio, request.getContrasena())
        );

        String jwt = tokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(guardado.getId())
                .nombre(guardado.getNombre())
                .correo(guardado.getCorreo())
                .rol(guardado.getRol().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse getPerfil(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));

        return AuthResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .build();
    }

    @Override
    public TokenValidationResponse validarToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (tokenProvider.validateToken(token)) {
            Claims claims = tokenProvider.getClaimsFromJwt(token);
            Number idNum = claims.get("id", Number.class);
            return TokenValidationResponse.builder()
                    .valid(true)
                    .username(claims.getSubject())
                    .nombre(claims.get("nombre", String.class))
                    .id(idNum != null ? idNum.longValue() : null)
                    .roles(claims.get("roles", String.class))
                    .build();
        }

        return TokenValidationResponse.builder().valid(false).build();
    }
}
