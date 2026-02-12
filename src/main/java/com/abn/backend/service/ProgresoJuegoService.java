package com.abn.backend.service;

import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import java.util.List;

public interface ProgresoJuegoService {
    List<ProgresoResponseDTO> obtenerProgresosPorInfante(Long infantId);
    ProgresoResponseDTO actualizarProgreso(Long id, ProgresoUpdateDTO dto);
}