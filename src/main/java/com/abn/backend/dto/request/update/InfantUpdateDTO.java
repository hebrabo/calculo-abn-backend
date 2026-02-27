package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DATA TRANSFER OBJECT (DTO) - ACTUALITZACIÓ D'INFANT
 * Aquest objecte actua com una "finestra" segura. Només exposem els camps
 * que el tutor té permís per modificar des de l'aplicació.
 */
@Data // Lombok: Automatitza el codi repetitiu (Getters i Setters).
// Això permet que el programador es centri en la definició de la dada i no en la seva estructura técnica.
public class InfantUpdateDTO {

    /**
     * @NotBlank: Protecció contra cadenes buides o només espais.
     * En una actualització (PUT), és vital per garantir que no sobreescrivim
     * un nom vàlid amb un camp buit per error des del client.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    /**
     * L'avatar es gestiona com un String que apunta al nom del recurs a Unity.
     * Validem que no sigui buit per mantenir la coherència visual de l'app.
     */
    @NotBlank(message = "El avatar no puede estar vacío")
    private String avatar;

    /**
     * VALIDACIÓ DE RANG (3-6 anys):
     * Aquesta és la nostra "regla de negoci" pedagògica.
     * @Min i @Max asseguren que, fins i tot en una edició, l'infant es mantingui
     * dins del segment d'edat on el mètode ABN és aplicable.
     */
    @Min(3)
    @Max(6)
    private int edad;
}