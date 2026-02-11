package com.abn.backend.service;

import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import java.util.List;

public interface InfantPerfilService {
    List<InfantResponseDTO> obtenerTodosLosInfantes();
    InfantResponseDTO obtenerInfantePorId(Long id);
    InfantResponseDTO crearInfante(InfantCreateDTO dto);
    InfantResponseDTO actualizarInfante(Long id, InfantUpdateDTO dto);
    void eliminarInfante(Long id);
}
