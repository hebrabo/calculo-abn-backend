package com.abn.backend.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.model.InfantPerfil;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class InfantPerfilMapper {

    private final ProgresoMapper progresoMapper;

    public InfantResponseDTO toDto(InfantPerfil infant) {
        if (infant == null) return null;

        InfantResponseDTO dto = new InfantResponseDTO();
        dto.setId(infant.getId());
        dto.setNombre(infant.getNombre());
        dto.setAvatar(infant.getAvatar());
        dto.setEdad(infant.getEdad());

        if (infant.getProgresos() != null) {
            dto.setProgresos(infant.getProgresos().stream()
                    .map(progresoMapper::toDto)
                    .toList());
        }
        return dto;
    }

    public InfantPerfil toEntity(InfantCreateDTO dto) {
        if (dto == null) return null;

        InfantPerfil infant = new InfantPerfil();
        infant.setNombre(dto.getNombre());
        infant.setAvatar(dto.getAvatar());
        infant.setEdad(dto.getEdad());
        return infant;
    }

    public void updateInfantFromDto(InfantUpdateDTO dto, InfantPerfil infant) {
        if (dto == null || infant == null) return;

        infant.setNombre(dto.getNombre());
        infant.setAvatar(dto.getAvatar());
        infant.setEdad(dto.getEdad());
    }
}
