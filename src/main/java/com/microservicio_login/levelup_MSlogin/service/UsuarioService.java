package com.microservicio_login.levelup_MSlogin.service;

import com.microservicio_login.levelup_MSlogin.model.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario obtenerPorId(Long id);

    Usuario obtenerPorCorreo(String correo);

    void cambiarRol(Long id, String nuevoRol);
}
