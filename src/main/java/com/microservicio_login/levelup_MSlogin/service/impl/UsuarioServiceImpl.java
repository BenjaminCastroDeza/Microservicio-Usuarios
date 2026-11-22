package com.microservicio_login.levelup_MSlogin.service.impl;

import com.microservicio_login.levelup_MSlogin.exception.BadRequestException;
import com.microservicio_login.levelup_MSlogin.exception.ResourceNotFoundException;
import com.microservicio_login.levelup_MSlogin.model.Role;
import com.microservicio_login.levelup_MSlogin.model.Usuario;
import com.microservicio_login.levelup_MSlogin.repository.UsuarioRepository;
import com.microservicio_login.levelup_MSlogin.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));
    }

    @Override
    @Transactional
    public Usuario crearUsuario(Map<String, String> datos) {
        String correo = datos.get("correo");
        if (correo == null || correo.isBlank()) {
            throw new BadRequestException("El correo es obligatorio.");
        }
        String correoLimpio = correo.trim().toLowerCase();
        if (usuarioRepository.existsByCorreo(correoLimpio)) {
            throw new BadRequestException("Ya existe un usuario con este correo electrónico.");
        }

        String contrasena = datos.get("contrasena");
        if (contrasena == null || contrasena.isBlank()) {
            contrasena = "User123!";
        }

        String rolStr = datos.getOrDefault("rol", "ROLE_CLIENTE");
        Role rol;
        try {
            rol = Role.valueOf(rolStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            rol = Role.ROLE_CLIENTE;
        }

        Usuario usuario = Usuario.builder()
                .nombre(datos.getOrDefault("nombre", "").trim())
                .correo(correoLimpio)
                .contrasena(passwordEncoder.encode(contrasena))
                .telefono(datos.get("telefono") != null ? datos.get("telefono").trim() : null)
                .region(datos.get("region") != null ? datos.get("region").trim() : "")
                .comuna(datos.get("comuna") != null ? datos.get("comuna").trim() : "")
                .rol(rol)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Map<String, String> datos) {
        Usuario usuario = obtenerPorId(id);

        if (datos.containsKey("nombre") && datos.get("nombre") != null && !datos.get("nombre").isBlank()) {
            usuario.setNombre(datos.get("nombre").trim());
        }
        if (datos.containsKey("telefono")) {
            usuario.setTelefono(datos.get("telefono") != null ? datos.get("telefono").trim() : null);
        }
        if (datos.containsKey("region")) {
            usuario.setRegion(datos.get("region") != null ? datos.get("region").trim() : "");
        }
        if (datos.containsKey("comuna")) {
            usuario.setComuna(datos.get("comuna") != null ? datos.get("comuna").trim() : "");
        }
        if (datos.containsKey("rol") && datos.get("rol") != null) {
            try {
                usuario.setRol(Role.valueOf(datos.get("rol").toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Rol inválido: " + datos.get("rol"));
            }
        }
        if (datos.containsKey("contrasena") && datos.get("contrasena") != null && !datos.get("contrasena").isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(datos.get("contrasena")));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void cambiarRol(Long id, String nuevoRol) {
        Usuario usuario = obtenerPorId(id);
        try {
            Role rolEnum = Role.valueOf(nuevoRol.toUpperCase());
            usuario.setRol(rolEnum);
            usuarioRepository.save(usuario);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Rol inválido: " + nuevoRol + ". Debe ser ROLE_CLIENTE o ROLE_ADMIN");
        }
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }
}
