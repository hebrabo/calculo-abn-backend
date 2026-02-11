package com.abn.backend.dto.response;

import lombok.Data;

@Data
public class ProgresoResponseDTO {
    private Long id;
    private long idJuego;
    private String nombreJuego;
    private boolean desbloqueado;
    private int estrellasGanadas;
}
