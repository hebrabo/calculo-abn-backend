package com.abn.backend.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class InfantResponseDTO {
    private Long id;
    private String nombre;
    private String avatar;
    private int edad;
    private List<ProgresoResponseDTO> progresos;
}
