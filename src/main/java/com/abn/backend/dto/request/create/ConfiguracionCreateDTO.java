package com.abn.backend.dto.request.create;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ConfiguracionCreateDTO {
    private boolean musicaActivada = true; //

    @Min(0) @Max(100)
    private int volumenEfectos = 80; //

    @NotBlank(message = "El idioma es obligatorio")
    private String idioma = "Castellano"; //
}