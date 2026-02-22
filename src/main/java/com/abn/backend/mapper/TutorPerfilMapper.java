package com.abn.backend.mapper;

import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.model.ConfiguracionTutor;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.model.InfantPerfil;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TutorPerfilMapper {

    private final ConfiguracionMapper configuracionMapper;

    public TutorResponseDTO toDto(TutorPerfil tutor) {
        if (tutor == null) return null;

        TutorResponseDTO dto = new TutorResponseDTO();
        dto.setId(tutor.getId());
        dto.setEmail(tutor.getEmail());

        // Mapeig de la configuració seguint el model del profe
        dto.setConfiguracion(configuracionMapper.toDto(tutor.getConfiguracion()));

        // Mapeig d'IDs de la llista d'infants (requisit per als 100 jocs)
        if (tutor.getInfantes() != null) {
            dto.setInfantes(tutor.getInfantes().stream().map(InfantPerfil::getId).toList());
        } else {
            dto.setInfantes(null);
        }

        return dto;
    }

    public TutorPerfil toEntity(TutorCreateDTO dto) {
        if (dto == null) return null;

        TutorPerfil tutor = new TutorPerfil();
        tutor.setEmail(dto.getEmail());

        // Usem el mapper de configuració per crear l'entitat niada
        if (dto.getConfiguracion() != null) {
            ConfiguracionTutor config = configuracionMapper.toEntity(dto.getConfiguracion());
            tutor.setConfiguracion(config); // El mètode sincronitzat de l'entitat
        }

        return tutor;
    }

    public void updateTutorFromDto(TutorUpdateDTO dto, TutorPerfil tutor) {
        if (dto == null || tutor == null) return;

        // Actualitzem l'email
        tutor.setEmail(dto.getEmail());

        // CANVI AQUÍ: El nom del mètode ha de ser exactament el que hi ha al ConfiguracionMapper
        if (dto.getConfiguracion() != null && tutor.getConfiguracion() != null) {
            configuracionMapper.updateConfigFromDto(dto.getConfiguracion(), tutor.getConfiguracion());
        }
    }
}