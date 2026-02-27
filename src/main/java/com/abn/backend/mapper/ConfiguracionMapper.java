package com.abn.backend.mapper;

import com.abn.backend.dto.request.create.ConfiguracionCreateDTO;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.model.ConfiguracionTutor;
import org.springframework.stereotype.Component;

/**
 * CAPA DE MAPEIG (Data Mapper Pattern)
 * Aquesta classe s'encarrega de la transformació d'objectes.
 * El seu objectiu és el DESACOBLAMENT: que la base de dades no conegui l'API,
 * i que l'API no conegui l'estructura interna de les taules.
 */
@Component // Indica a Spring que aquesta classe és un Bean i es pot injectar on calgui (@Autowired).
public class ConfiguracionMapper {

    /**
     * ENTITAT -> DTO (Sortida cap a Unity)
     * Transforma una fila de la base de dades en un objecte JSON segur.
     */
    public ConfiguracionResponseDTO toDto(ConfiguracionTutor entity) {
        if (entity == null) return null;

        ConfiguracionResponseDTO dto = new ConfiguracionResponseDTO();
        dto.setId(entity.getId());
        dto.setMusicaActivada(entity.isMusicaActivada());
        dto.setVolumenEfectos(entity.getVolumenEfectos());
        dto.setIdioma(entity.getIdioma());
        return dto;
    }

    /**
     * DTO -> ENTITAT (Entrada des de Unity)
     * Converteix les dades del formulari de registre en un objecte que JPA pot persistir.
     */
    public ConfiguracionTutor toEntity(ConfiguracionCreateDTO dto) {
        if (dto == null) return null;

        // Creem una nova instància de l'entitat de base de dades
        ConfiguracionTutor entity = new ConfiguracionTutor();

        // Mapegem els camps manualment (si no fem servir llibreries com MapStruct)
        entity.setMusicaActivada(dto.isMusicaActivada());
        entity.setVolumenEfectos(dto.getVolumenEfectos());
        entity.setIdioma(dto.getIdioma());

        return entity;
    }

    /**
     * ACTUALITZACIÓ SOBRE ENTITAT EXISTENT
     * Aquest mètode és vital per als 'PUT'. En lloc de crear un objecte nou,
     * modifiquem el que ja hem recuperat de la base de dades (estat 'Managed' de JPA).
     */
    public void updateConfigFromDto(ConfiguracionUpdateDTO dto, ConfiguracionTutor existente) {
        if (dto == null || existente == null) return;

        // Sobreescriure els camps de l'entitat que resideix a la DB amb les noves dades
        existente.setMusicaActivada(dto.isMusicaActivada());
        existente.setVolumenEfectos(dto.getVolumenEfectos());
        existente.setIdioma(dto.getIdioma());
    }
}