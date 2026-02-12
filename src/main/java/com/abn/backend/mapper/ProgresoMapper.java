package com.abn.backend.mapper;

import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.model.ProgresoJuego;
import org.springframework.stereotype.Component;

@Component
public class ProgresoMapper {

    public ProgresoResponseDTO toDto(ProgresoJuego progreso) {
        if (progreso == null) return null;

        ProgresoResponseDTO dto = new ProgresoResponseDTO();
        dto.setId(progreso.getId());
        dto.setIdJuego(progreso.getIdJuego());
        dto.setNombreJuego(progreso.getNombreJuego());
        dto.setDesbloqueado(progreso.isDesbloqueado());
        dto.setEstrellasGanadas(progreso.getEstrellasGanadas());

        // MAPEIG DE LES NOVES MÈTRIQUES
        dto.setTiempoSegundos(progreso.getTiempoSegundos());
        dto.setIntentosFallidos(progreso.getIntentosFallidos());

        return dto;
    }
}