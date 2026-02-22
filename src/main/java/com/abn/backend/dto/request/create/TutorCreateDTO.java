package com.abn.backend.dto.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TutorCreateDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    @Valid
    @NotNull(message = "La configuración es obligatoria.")
    private ConfiguracionCreateDTO configuracion;
}