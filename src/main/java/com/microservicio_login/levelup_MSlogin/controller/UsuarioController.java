package com.microservicio_login.levelup_MSlogin.controller;

import com.microservicio_login.levelup_MSlogin.dto.ApiResponse;
import com.microservicio_login.levelup_MSlogin.model.Usuario;
import com.microservicio_login.levelup_MSlogin.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios (Admin)", description = "Endpoints de administración de usuarios y asignación de roles")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna la lista de usuarios registrados en el sistema.")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna la información de un usuario específico.")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un usuario desde el panel de administración.")
    public ResponseEntity<Usuario> crear(@RequestBody Map<String, String> body) {
        return new ResponseEntity<>(usuarioService.crearUsuario(body), org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario por su ID.")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema.")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado correctamente", null));
    }

    @PatchMapping("/{id}/rol")
    @Operation(summary = "Cambiar rol de usuario", description = "Modifica el rol de un usuario (ej: ROLE_ADMIN o ROLE_CLIENTE).")
    public ResponseEntity<ApiResponse<Void>> cambiarRol(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String nuevoRol = body.get("rol");
        usuarioService.cambiarRol(id, nuevoRol);
        return ResponseEntity.ok(ApiResponse.ok("Rol actualizado correctamente", null));
    }
}
