package com.abn.backend.service;

import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import java.util.List;

public interface TutorPerfilService {
    List<TutorResponseDTO> obtenerTodosLosTutores();
    TutorResponseDTO obtenerTutorPorId(Long id);
    TutorResponseDTO crearTutor(TutorCreateDTO dto);
    TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto);
    void eliminarTutor(Long id);
}
