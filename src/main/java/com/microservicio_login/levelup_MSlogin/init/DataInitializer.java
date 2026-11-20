package com.microservicio_login.levelup_MSlogin.init;

import com.microservicio_login.levelup_MSlogin.model.Role;
import com.microservicio_login.levelup_MSlogin.model.Usuario;
import com.microservicio_login.levelup_MSlogin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByCorreo("admin@levelup.cl")) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador LevelUp")
                    .correo("admin@levelup.cl")
                    .contrasena(passwordEncoder.encode("Admin123!"))
                    .telefono("+56 9 9999 8888")
                    .region("Region Metropolitana")
                    .comuna("Santiago")
                    .rol(Role.ROLE_ADMIN)
                    .build();
            usuarioRepository.save(admin);
            log.info("✓ [MS-Login] Usuario Administrador inicial creado: admin@levelup.cl / Admin123!");
        }

        if (!usuarioRepository.existsByCorreo("cliente@levelup.cl")) {
            Usuario cliente = Usuario.builder()
                    .nombre("Benjamin Castro")
                    .correo("cliente@levelup.cl")
                    .contrasena(passwordEncoder.encode("User123!"))
                    .telefono("+56 9 1234 5678")
                    .region("Region de Valparaiso")
                    .comuna("Viña del Mar")
                    .rol(Role.ROLE_CLIENTE)
                    .build();
            usuarioRepository.save(cliente);
            log.info("✓ [MS-Login] Usuario Cliente inicial creado: cliente@levelup.cl / User123!");
        }
    }
}
