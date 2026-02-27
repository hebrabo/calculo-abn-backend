package com.abn.backend.dto.response;

import lombok.Data;
import java.util.List;

/**
 * DTO DE RESPOSTA - PERFIL DE L'INFANT
 * Aquest objecte és el que rep Unity per dibuixar les "Targetes de Perfil".
 * És un DTO d'agregació: conté dades pròpies i una llista de dades vinculades.
 */
@Data // Lombok: Genera automàticament Getters i Setters per a la serialització a JSON.
public class InfantResponseDTO {

    /**
     * ID Remota (Clau Primària a AWS RDS).
     * Imprescindible perquè el client Unity pugui fer referència a aquest infant
     * en futures peticions (com actualitzar el seu progrés).
     */
    private Long id;

    private String nombre;
    private String avatar;
    private int edad;

    /**
     * REFERÈNCIA AL TUTOR (Foreign Key):
     * En lloc d'incloure tot l'objecte Tutor (que pesaria massa i crearia cicles),
     * només enviem la seva ID per mantenir la traçabilitat del recurs.
     */
    private Long tutorId;

    /**
     * RELACIÓ ONE-TO-MANY (1:N):
     * Aquest és el punt clau d'Accés a Dades. Aquí estem enviant el "Detall"
     * dels progressos del nen. Unity fa servir aquesta llista per saber quins
     * dels 100 jocs estan bloquejats o quines estrelles té cadascun.
     */
    private List<ProgresoResponseDTO> progresos;
}