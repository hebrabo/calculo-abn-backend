package com.abn.backend.dto.request.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TutorUpdateDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    // Afegim la configuració per poder actualitzar música, volum i idioma
    @Valid
    private ConfiguracionUpdateDTO configuracion;
}