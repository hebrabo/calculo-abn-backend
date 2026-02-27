package com.abn.backend.dto.response;

import lombok.Data;

/**
 * DATA TRANSFER OBJECT (DTO) - RESPOSTA DE CONFIGURACIÓ
 * Aquest objecte és la "funda" que embolcalla les dades que surten de la base de dades
 * d'AWS per ser enviades al client (Unity).
 */
@Data // Lombok: Genera Getters i Setters.
// Imprescindible perquè Spring pugui convertir aquest objecte a JSON (serialització).
public class ConfiguracionResponseDTO {

    /**
     * ID de la configuració.
     * Encara que a la DB és una Clau Primària, aquí serveix perquè el client
     * sàpiga exactament quin recurs està consultant o ha de modificar.
     */
    private Long id;

    // Estat actual de la música en el perfil del tutor.
    private boolean musicaActivada;

    // Nivell de volum (0-100) guardat a la persistència.
    private int volumenEfectos;

    // Idioma seleccionat (Català, Castellà, etc.).
    private String idioma;
}