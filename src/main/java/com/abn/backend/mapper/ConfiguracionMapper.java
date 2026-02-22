package com.abn.backend.mapper;

import com.abn.backend.dto.request.create.ConfiguracionCreateDTO;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.model.ConfiguracionTutor;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionMapper {

    public ConfiguracionResponseDTO toDto(ConfiguracionTutor entity) {
        if (entity == null) return null;
        ConfiguracionResponseDTO dto = new ConfiguracionResponseDTO();
        dto.setId(entity.getId());
        dto.setMusicaActivada(entity.isMusicaActivada());
        dto.setVolumenEfectos(entity.getVolumenEfectos());
        dto.setIdioma(entity.getIdioma());
        return dto;
    }

    // Aquest és el mètode que "cannot resolve" IntelliJ
    public ConfiguracionTutor toEntity(ConfiguracionCreateDTO dto) {
        if (dto == null) return null;

        ConfiguracionTutor entity = new ConfiguracionTutor();
        entity.setMusicaActivada(dto.isMusicaActivada());
        entity.setVolumenEfectos(dto.getVolumenEfectos());
        entity.setIdioma(dto.getIdioma());
        return entity;
    }

    public void updateConfigFromDto(ConfiguracionUpdateDTO dto, ConfiguracionTutor existente) {
        if (dto == null || existente == null) return;

        // Actualitzem els camps de l'entitat existent amb les dades del DTO
        existente.setMusicaActivada(dto.isMusicaActivada());
        existente.setVolumenEfectos(dto.getVolumenEfectos());
        existente.setIdioma(dto.getIdioma());
    }
}