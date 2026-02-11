package com.abn.backend.dto.response;

import lombok.Data;

@Data
public class ConfiguracionResponseDTO {
    private Long id;
    private boolean musicaActivada;
    private int volumenEfectos;
    private String idioma;
}
