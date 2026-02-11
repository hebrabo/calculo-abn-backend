package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.mapper.ConfiguracionMapper;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import com.abn.backend.service.ConfiguracionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final TutorPerfilRepository tutorRepository;
    private final ConfiguracionMapper configuracionMapper;

    @Override
    @Transactional
    public ConfiguracionResponseDTO actualizarConfiguracion(Long tutorId, ConfiguracionUpdateDTO dto) {
        TutorPerfil tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado"));

        configuracionMapper.updateConfigFromDto(dto, tutor.getConfiguracion());
        return configuracionMapper.toDto(tutorRepository.save(tutor).getConfiguracion());
    }

    @Override
    public ConfiguracionResponseDTO obtenerConfiguracionPorTutor(Long tutorId) {
        TutorPerfil tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado"));
        return configuracionMapper.toDto(tutor.getConfiguracion());
    }
}