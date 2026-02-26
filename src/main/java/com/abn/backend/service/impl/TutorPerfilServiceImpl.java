package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.dto.response.InfantResponseDTO; // <--- IMPORTANTE
import com.abn.backend.mapper.TutorPerfilMapper;
import com.abn.backend.mapper.InfantPerfilMapper; // <--- IMPORTANTE (Mapper de los niños)
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import com.abn.backend.service.TutorPerfilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TutorPerfilServiceImpl implements TutorPerfilService {

    private final TutorPerfilRepository tutorRepository;
    private final TutorPerfilMapper tutorMapper;
    private final InfantPerfilMapper infantMapper; // Inyectamos el mapper de los infantes

    @Override
    @Transactional(readOnly = true)
    public List<TutorResponseDTO> obtenerTodosLosTutores() {
        return tutorRepository.findAll()
                .stream()
                .map(tutorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TutorResponseDTO obtenerTutorPorId(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tutor con id " + id + " no encontrado"));
        return tutorMapper.toDto(tutor);
    }

    // --- MÈTODE PER A UNITY: Llista els infants detallats d'un tutor ---
    @Override
    @Transactional(readOnly = true)
    public List<InfantResponseDTO> obtenerInfantesPorTutor(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // Transformamos cada entidad Infante del tutor en un DTO completo para Unity
        return tutor.getInfantes()
                .stream()
                .map(infantMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TutorResponseDTO crearTutor(TutorCreateDTO dto) {
        TutorPerfil tutor = tutorMapper.toEntity(dto);
        TutorPerfil tutorGuardado = tutorRepository.save(tutor);
        return tutorMapper.toDto(tutorGuardado);
    }

    @Override
    @Transactional
    public TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto) {
        TutorPerfil existente = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        tutorMapper.updateTutorFromDto(dto, existente);
        return tutorMapper.toDto(tutorRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminarTutor(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        if (tutor.getInfantes() != null && !tutor.getInfantes().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un tutor con infantes asociados.");
        }

        tutorRepository.delete(tutor);
    }
}