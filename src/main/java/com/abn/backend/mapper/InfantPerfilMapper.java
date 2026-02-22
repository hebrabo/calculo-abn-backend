package com.abn.backend.mapper;

import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.model.InfantPerfil;
import org.springframework.stereotype.Component;

@Component
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

        // --- SOLUCIÓ AL TUTOR ID NULL ---
        // Extraiem l'ID de l'objecte tutor vinculat a l'entitat
        if (infant.getTutor() != null) {
            dto.setTutorId(infant.getTutor().getId());
        }

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
        // El tutor es vincula al Service usant el tutorId del DTO
        return infant;
    }

    public void updateInfantFromDto(InfantUpdateDTO dto, InfantPerfil infant) {
        if (dto == null || infant == null) return;

        if (dto.getNombre() != null) infant.setNombre(dto.getNombre());
        if (dto.getAvatar() != null) infant.setAvatar(dto.getAvatar());
        if (dto.getEdad() > 0) infant.setEdad(dto.getEdad());
    }
}