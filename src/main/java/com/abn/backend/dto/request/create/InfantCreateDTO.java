package com.abn.backend.dto.request.create;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DATA TRANSFER OBJECT (DTO) - CREACIÓ D'INFANT
 * Aquest objecte s'encarrega de recollir les dades del formulari de Unity
 * i validar-les abans que el Servei les converteixi en una Entitat JPA.
 */
@Data // Lombok: Genera automàticament Getters, Setters i el constructor.
// Millora la llegibilitat i redueix el manteniment del codi.
public class InfantCreateDTO {

    /**
     * @NotBlank: No permet nulls, ni cadenes buides (""), ni només espais (" ").
     * És més exigent que @NotNull per a camps de text.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre es demasiado largo")
    private String nombre;

    /**
     * L'avatar es rep com el nom del sprite seleccionat a Unity (Ex: "avatar_1").
     */
    @NotBlank(message = "Debes seleccionar un avatar")
    private String avatar;

    /**
     * VALIDACIÓ DE LÒGICA DE NEGOCI:
     * El mètode ABN se centra en l'etapa d'infantil.
     * @Min i @Max asseguren que només registrem nens dins del rang pedagògic correcte.
     */
    @Min(value = 3, message = "La edad mínima es de 3 años")
    @Max(value = 6, message = "La edad máxima es de 6 años")
    private int edad;

    /**
     * CLAU ESTRANGERA (FK):
     * El DTO no rep l'objecte Tutor sencer, sinó només la seva ID.
     * @NotNull: Garanteix que cap infant es quedi "orfe" a la base de dades,
     * mantenint la integritat referencial.
     */
    @NotNull(message = "El ID del tutor es obligatorio")
    private Long tutorId;
}