package com.abn.backend.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.model.TutorPerfil;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class TutorPerfilMapper {

    private final ConfiguracionMapper configuracionMapper;

    public TutorResponseDTO toDto(TutorPerfil tutor) {
        if (tutor == null) return null;

        TutorResponseDTO dto = new TutorResponseDTO();
        dto.setId(tutor.getId());
        dto.setEmail(tutor.getEmail());
        dto.setConfiguracion(configuracionMapper.toDto(tutor.getConfiguracion()));
        return dto;
    }

    public TutorPerfil toEntity(TutorCreateDTO dto) {
        if (dto == null) return null;

        TutorPerfil tutor = new TutorPerfil();
        tutor.setEmail(dto.getEmail());
        return tutor;
    }
}
