package com.abn.backend.service;

import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;

public interface ConfiguracionService {
    ConfiguracionResponseDTO actualizarConfiguracion(Long tutorId, ConfiguracionUpdateDTO dto);
    ConfiguracionResponseDTO obtenerConfiguracionPorTutor(Long tutorId);
}
