package com.abn.backend.mapper;

import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.model.InfantPerfil;
import org.springframework.stereotype.Component;

/**
 * CAPA DE MAPEIG - GESTIÓ DE PERFILS INFANTILS
 * Aquesta classe transforma les entitats de JPA (persistència) en objectes DTO (presentació).
 * És crucial per evitar que la lògica de base de dades "contamini" la interfície d'usuari.
 */
@Component // Registra la classe com a Bean de Spring per poder ser injectada.
@RequiredArgsConstructor // Lombok: Genera el constructor per a la injecció del 'progresoMapper'.
public class InfantPerfilMapper {

    // Injectem un altre mapper per gestionar la col·lecció de progressos (composició de mappers)
    private final ProgresoMapper progresoMapper;

    /**
     * ENTITAT -> DTO
     * Converteix l'objecte de la base de dades en una resposta per a Unity.
     */
    public InfantResponseDTO toDto(InfantPerfil infant) {
        if (infant == null) return null;

        InfantResponseDTO dto = new InfantResponseDTO();
        dto.setId(infant.getId());
        dto.setNombre(infant.getNombre());
        dto.setAvatar(infant.getAvatar());
        dto.setEdad(infant.getEdad());

        /**
         * GESTIÓ DE RELACIONS (Flattening):
         * En lloc d'enviar tot l'objecte Tutor (que crearia un JSON circular),
         * només n'extraiem la ID. Això es coneix com a "aplanar" el model.
         */
        if (infant.getTutor() != null) {
            dto.setTutorId(infant.getTutor().getId());
        }

        /**
         * MAPEIG DE COL·LECCIONS (1:N):
         * Utilitzem l'API Stream de Java 8+ per transformar la llista d'entitats
         * 'Progreso' en una llista de DTOs de forma funcional i eficient.
         */
        if (infant.getProgresos() != null) {
            dto.setProgresos(infant.getProgresos().stream()
                    .map(progresoMapper::toDto) // Deleguem el mapeig de cada progrés al seu propi mapper
                    .toList());
        }
        return dto;
    }

    /**
     * DTO -> ENTITAT
     * Crea un nou objecte InfantPerfil per ser desat a la base de dades.
     */
    public InfantPerfil toEntity(InfantCreateDTO dto) {
        if (dto == null) return null;

        InfantPerfil infant = new InfantPerfil();
        infant.setNombre(dto.getNombre());
        infant.setAvatar(dto.getAvatar());
        infant.setEdad(dto.getEdad());

        // Nota didàctica: La vinculació amb el Tutor (ID -> Objecte) es fa al Service
        // perquè requereix una consulta prèvia al repositori.
        return infant;
    }

    /**
     * ACTUALITZACIÓ PARCIAL (Dirty Checking):
     * Permet modificar només els camps enviats, mantenint la integritat de l'objecte original.
     */
    public void updateInfantFromDto(InfantUpdateDTO dto, InfantPerfil infant) {
        if (dto == null || infant == null) return;

        // Verificacions de seguretat abans de sobreescriure dades a la DB
        if (dto.getNombre() != null) infant.setNombre(dto.getNombre());
        if (dto.getAvatar() != null) infant.setAvatar(dto.getAvatar());
        if (dto.getEdad() > 0) infant.setEdad(dto.getEdad());
    }
}