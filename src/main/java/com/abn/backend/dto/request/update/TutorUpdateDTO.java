package com.abn.backend.dto.request.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO PER A L'ACTUALITZACIÓ DEL TUTOR
 * Aquest objecte gestiona els canvis en el perfil principal de l'usuari.
 * Implementa el concepte de "Composició", ja que inclou la configuració niuada.
 */
@Data // Lombok: Automatitza Getters i Setters.
// Estalvia codi repetitiu i facilita que Spring/Jackson mapegin el JSON de Unity a l'objecte Java.
public class TutorUpdateDTO {

    /**
     * @NotBlank: Garanteix que no enviem un email buit.
     * @Email: Una de les validacions més importants d'Accés a Dades.
     * El servidor verifica que la cadena tingui un format d'adreça vàlid
     * abans d'intentar actualitzar la fila a la taula 'tutores' d'AWS.
     */
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    /**
     * VALIDACIÓ NIUADA (EN CASCADA):
     * @Valid: Aquesta anotació és vital en un examen d'arquitectura.
     * Indica a Spring que, en rebre aquest objecte, també ha d'executar
     * les validacions de 'ConfiguracionUpdateDTO' (com els rangs del volum).
     * Sense @Valid, Spring ignoraria les regles de validació de l'objecte fill.
     */
    @Valid
    private ConfiguracionUpdateDTO configuracion;
}