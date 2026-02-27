package com.abn.backend.dto.request.create;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DATA TRANSFER OBJECT (DTO) - CREACIÓ DE CONFIGURACIÓ
 * Aquest objecte s'encarrega de recollir les preferències de l'usuari (Unity)
 * i transportar-les cap al servei de Spring Boot de forma segura.
 */
@Data // Lombok: Genera automàticament Getters, Setters, equals, hashCode i toString.
// Estalvia "boilerplate code" i manté la classe neta i llegible.
public class ConfiguracionCreateDTO {

    // Valor per defecte: La música estarà activada si no s'especifica el contrari.
    private boolean musicaActivada = true;

    /**
     * BEAN VALIDATION: Regles de rang.
     * @Min i @Max asseguren que el volum estigui en un rang lògic (0-100).
     * Això evita que dades corruptes o absurdes arribin a la columna de la DB.
     */
    @Min(0)
    @Max(100)
    private int volumenEfectos = 80;

    /**
     * BEAN VALIDATION: Regles de cadena.
     * @NotBlank: Verifica que l'idioma no sigui null, ni estigui buit,
     * ni contingui només espais en blanc.
     * És vital per a dades obligatòries de tipus String.
     */
    @NotBlank(message = "El idioma es obligatorio")
    private String idioma = "Castellano";
}