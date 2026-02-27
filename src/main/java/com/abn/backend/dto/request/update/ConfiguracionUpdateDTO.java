package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DATA TRANSFER OBJECT (DTO) - ACTUALITZACIÓ DE CONFIGURACIÓ
 * Aquest objecte s'utilitza exclusivament per a les operacions de modificació (PUT).
 * Separa les dades que el client (Unity) pot enviar per actualitzar de les que
 * romanen immutables a la base de dades.
 */
@Data // Lombok: Genera Getters, Setters, equals, hashCode i toString.
// Imprescindible per mantenir el codi "Boilerplate" al mínim i centrar-nos en la dada.
public class ConfiguracionUpdateDTO {

    // No necessita validació específica, és un booleà simple per a l'estat de la música.
    private boolean musicaActivada;

    /**
     * BEAN VALIDATION: Control de rang numèric.
     * @Min(0) i @Max(100): Garanteixen que el volum que ens arriba de Unity
     * estigui dins de l'escala lògica. Això evita errors de desbordament o
     * valors absurds en el processament de so.
     */
    @Min(0)
    @Max(100)
    private int volumenEfectos;

    /**
     * BEAN VALIDATION: Control de cadenes de text.
     * @NotBlank: Molt important en actualitzacions per evitar que un error
     * a Unity enviï una cadena buida i "esborri" l'idioma configurat a la DB.
     */
    @NotBlank(message = "El idioma no puede estar vacío")
    private String idioma;
}