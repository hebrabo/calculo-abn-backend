package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.mapper.TutorPerfilMapper;
import com.abn.backend.mapper.InfantPerfilMapper;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import com.abn.backend.service.TutorPerfilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * IMPLEMENTACIÓ DEL SERVEI DE TUTORS
 * Aquest component gestiona l'accés principal a l'aplicació.
 * És l'encarregat de lligar la configuració global i els perfils dels infants.
 */
@Service // Indica a Spring que aquesta classe conté la lògica de negoci del tutor.
@RequiredArgsConstructor // Injecció de dependències per constructor (Bona pràctica de Spring).
public class TutorPerfilServiceImpl implements TutorPerfilService {

    private final TutorPerfilRepository tutorRepository;
    private final TutorPerfilMapper tutorMapper;
    private final InfantPerfilMapper infantMapper; // Necessari per transformar els fills en la resposta detallada.

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

    /**
     * MÈTODE PER A UNITY: Recuperació detallada de la família.
     * @Transactional(readOnly = true): Avisem a la base de dades que només llegirem,
     * el que millora el rendiment en l'entorn d'AWS RDS.
     */
    @Override
    @Transactional(readOnly = true)
    public List<InfantResponseDTO> obtenerInfantesPorTutor(Long id) {
        // Busquem el tutor o llancem error si la ID de Unity no existeix al servidor.
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        /**
         * MAPEIG DE RELACIÓ 1:N:
         * Accedim a la col·lecció d'entitats 'InfantPerfil' del tutor i les
         * transformem en DTOs detallats (amb els seus progressos) per al client.
         */
        return tutor.getInfantes()
                .stream()
                .map(infantMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TutorResponseDTO crearTutor(TutorCreateDTO dto) {
        TutorPerfil tutor = tutorMapper.toEntity(dto);
        // El save() de JPA generarà un INSERT a la taula 'tutores_perfil' i 'configuraciones_tutor' (cascada).
        TutorPerfil tutorGuardado = tutorRepository.save(tutor);
        return tutorMapper.toDto(tutorGuardado);
    }

    @Override
    @Transactional
    public TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto) {
        TutorPerfil existente = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // Apliquem els canvis sobre l'entitat gestionada per JPA (Dirty Checking).
        tutorMapper.updateTutorFromDto(dto, existente);
        return tutorMapper.toDto(tutorRepository.save(existente));
    }

    /**
     * ELIMINACIÓ AMB RESTRICCIÓ DE NEGOCI:
     * En lloc de fer un esborrat a cegues, verifiquem si el tutor té infants.
     */
    @Override
    @Transactional
    public void eliminarTutor(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // Regla de seguretat: No permetem esborrar un tutor si té dades de nens actives.
        if (tutor.getInfantes() != null && !tutor.getInfantes().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un tutor con infantes asociados.");
        }

        tutorRepository.delete(tutor);
    }
}