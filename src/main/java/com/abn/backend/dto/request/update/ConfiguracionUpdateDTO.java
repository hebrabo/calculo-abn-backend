package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ConfiguracionUpdateDTO {
    private boolean musicaActivada;

    @Min(0) @Max(100)
    private int volumenEfectos;

    @NotBlank(message = "El idioma no puede estar vacío")
    private String idioma;
}
