package com.microservicio_login.levelup_MSlogin.service.impl;

import com.microservicio_login.levelup_MSlogin.exception.BadRequestException;
import com.microservicio_login.levelup_MSlogin.exception.ResourceNotFoundException;
import com.microservicio_login.levelup_MSlogin.model.Role;
import com.microservicio_login.levelup_MSlogin.model.Usuario;
import com.microservicio_login.levelup_MSlogin.repository.UsuarioRepository;
import com.microservicio_login.levelup_MSlogin.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

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
}
