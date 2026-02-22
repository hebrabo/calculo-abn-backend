package com.abn.backend.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class TutorResponseDTO {
    private Long id;
    private String email;
    private ConfiguracionResponseDTO configuracion;

    // Afegim aquest camp per guardar els IDs dels nens associats
    private List<Long> infantes;
}