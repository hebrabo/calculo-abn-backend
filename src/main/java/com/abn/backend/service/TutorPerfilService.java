package com.abn.backend.service;

import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.dto.response.InfantResponseDTO; // <--- Importem el DTO d'infants
import java.util.List;

/**
 * Interfície per al servei de Tutors.
 * Defineix les operacions de gestió de tutors i la seva relació amb els infants.
 */
public interface TutorPerfilService {

    List<TutorResponseDTO> obtenerTodosLosTutores();

    TutorResponseDTO obtenerTutorPorId(Long id);

    TutorResponseDTO crearTutor(TutorCreateDTO dto);

    TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto);

    void eliminarTutor(Long id);

    List<InfantResponseDTO> obtenerInfantesPorTutor(Long id);
}