package com.microservicio_login.levelup_MSlogin.service;

import com.microservicio_login.levelup_MSlogin.model.Usuario;

import java.util.List;
import java.util.Map;

public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario obtenerPorId(Long id);

    Usuario obtenerPorCorreo(String correo);

    Usuario crearUsuario(Map<String, String> datos);

    Usuario actualizarUsuario(Long id, Map<String, String> datos);

    void cambiarRol(Long id, String nuevoRol);

    void eliminarUsuario(Long id);
}
