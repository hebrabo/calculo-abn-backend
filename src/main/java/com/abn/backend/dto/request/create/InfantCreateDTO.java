package com.abn.backend.dto.request.create;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InfantCreateDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre es demasiado largo")
    private String nombre;

    @NotBlank(message = "Debes seleccionar un avatar")
    private String avatar;

    @Min(value = 3, message = "La edad mínima es de 3 años")
    @Max(value = 6, message = "La edad máxima es de 6 años")
    private int edad;

    @NotNull(message = "El ID del tutor es obligatorio")
    private Long tutorId;
}
