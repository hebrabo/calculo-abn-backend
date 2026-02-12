package com.abn.backend.dto.response;

import lombok.Data;

@Data
public class ProgresoResponseDTO {
    private Long id;
    private long idJuego;
    private String nombreJuego;
    private boolean desbloqueado;
    private int estrellasGanadas;

    // TAMBÉ LES AFEGIM AQUÍ PERQUÈ UNITY LES REBI
    private double tiempoSegundos;
    private int intentosFallidos;
}