package com.abn.backend.dto.response;

import lombok.Data;

@Data
public class TutorResponseDTO {
    private Long id;
    private String email;
    private ConfiguracionResponseDTO configuracion;
}
