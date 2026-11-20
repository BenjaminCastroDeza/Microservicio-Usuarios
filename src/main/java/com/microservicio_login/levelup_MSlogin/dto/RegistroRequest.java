package com.microservicio_login.levelup_MSlogin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre sólo debe contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Ingrese un formato de correo válido (ej: usuario@mail.com)")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&.])[A-Za-z\\d@#$!%*?&.]{8,}$",
        message = "Debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo (@#$%!.*)"
    )
    private String contrasena;

    @Pattern(
        regexp = "^$|^(\\+?56\\s?9\\s?\\d{4}\\s?\\d{4}|9\\d{8})$",
        message = "Formato de teléfono móvil chileno inválido (+56 9 1234 5678 o 912345678)"
    )
    private String telefono;

    @NotBlank(message = "Debe seleccionar una región")
    private String region;

    @NotBlank(message = "Debe seleccionar una comuna")
    private String comuna;
}
