package com.abn.backend.dto.response;

import lombok.Data;

/**
 * Resposta que el servidor envia a Unity.
 * Conté tota l'analítica necessària per a la sincronització inicial (Splash).
 */
@Data
public class ProgresoResponseDTO {
    private Long id;                // Vincularà amb 'id_remot' a la SQLite
    private long idJuego;           // ID de l'activitat (Ex: 1301)
    private String nombreJuego;
    private boolean desbloqueado;
    private int estrellasGanadas;

    // Analítica per al seguiment del tutor
    private double tiempoSegundos;
    private int intentosFallidos;
}