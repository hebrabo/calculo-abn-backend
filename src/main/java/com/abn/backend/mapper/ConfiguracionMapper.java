package com.abn.backend.mapper;

import lombok.Data;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.model.ConfiguracionTutor;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfiguracionMapper {

    public ConfiguracionResponseDTO toDto(ConfiguracionTutor config) {
        if (config == null) return null;

        ConfiguracionResponseDTO dto = new ConfiguracionResponseDTO();
        dto.setId(config.getId());
        dto.setMusicaActivada(config.isMusicaActivada());
        dto.setVolumenEfectos(config.getVolumenEfectos());
        dto.setIdioma(config.getIdioma());
        return dto;
    }

    public void updateConfigFromDto(ConfiguracionUpdateDTO dto, ConfiguracionTutor config) {
        if (dto == null || config == null) return;

        config.setMusicaActivada(dto.isMusicaActivada());
        config.setVolumenEfectos(dto.getVolumenEfectos());
        config.setIdioma(dto.getIdioma());
    }
}
