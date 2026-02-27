package com.abn.backend.dto.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DATA TRANSFER OBJECT (DTO) - REGISTRE DE TUTOR
 * Aquest DTO és el punt d'entrada per als nous usuaris (mestres/pares).
 * Representa una relació de composició: Un Tutor neix amb una Configuració.
 */
@Data // Lombok: Automatitza el codi repetitiu (Getters/Setters).
// Millora la mantenibilitat i evita errors humans al picar codi.
public class TutorCreateDTO {

    /**
     * @NotBlank: Comprovació inicial de text.
     * @Email: Una de les validacions més potents de Jakarta.
     * Verifica automàticament que la cadena segueixi el patró estàndard (user@domain.com)
     * mitjançant una expressió regular interna, abans d'arribar a la base de dades.
     */
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    /**
     * VALIDACIÓ ANIDADA (RECURSIVA):
     * @Valid: Aquesta anotació és crucial. Indica a Spring que no només ha de
     * validar que l'objecte 'configuracion' existeixi (@NotNull), sinó que també
     * ha d'entrar dins d'ell i validar les seves pròpies regles (@Min, @Max, etc.).
     * * @NotNull: Assegura que el tutor sempre tingui preferències associades,
     * mantenint la integritat del model de dades des del primer segon.
     */
    @Valid
    @NotNull(message = "La configuración es obligatoria.")
    private ConfiguracionCreateDTO configuracion;
}