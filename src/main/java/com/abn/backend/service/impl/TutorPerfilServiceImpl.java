package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.mapper.TutorPerfilMapper;
import com.abn.backend.model.ConfiguracionTutor;
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

    @Override
    public List<TutorResponseDTO> obtenerTodosLosTutores() {
        // Ús del mètode estàndard findAll()
        return tutorRepository.findAll()
                .stream()
                .map(tutorMapper::toDto)
                .toList();
    }

    @Override
    public TutorResponseDTO obtenerTutorPorId(Long id) {
        // Ús del mètode estàndard findById()
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tutor con id " + id + " no encontrado"));
        return tutorMapper.toDto(tutor);
    }

    @Override
    @Transactional
    public TutorResponseDTO crearTutor(TutorCreateDTO dto) {
        // Estil Profe: Busquem l'email filtrant la llista completa per deixar el repo buit
        boolean existe = tutorRepository.findAll().stream()
                .anyMatch(t -> t.getEmail().equalsIgnoreCase(dto.getEmail()));

        if (existe) {
            throw new IllegalStateException("El email ya está registrado");
        }

        TutorPerfil tutor = tutorMapper.toEntity(dto);

        // Inicialitzem la configuració per defecte (Relació 1:1 requerida al PDF)
        ConfiguracionTutor config = new ConfiguracionTutor();
        config.setMusicaActivada(true);
        config.setVolumenEfectos(80);
        config.setIdioma("Castellano");
        tutor.setConfiguracion(config);

        return tutorMapper.toDto(tutorRepository.save(tutor));
    }

    @Override
    @Transactional
    public TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto) {
        // 1. Cercar l'entitat real (Estil Profe)
        TutorPerfil existente = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // 2. Actualitzar camps (Lògica de negoci de l'email)
        existente.setEmail(dto.getEmail());

        // 3. Guardar l'entitat modificada
        return tutorMapper.toDto(tutorRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminarTutor(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // Comprovació de Relació 1:N (Requisit de Lògica de Negoci del PDF) [cite: 15, 18]
        // Evitem esborrar un tutor si encara té infants de 3-5 anys associats [cite: 2026-01-06]
        if (tutor.getInfantes() != null && !tutor.getInfantes().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un tutor con infantes asociados.");
        }

        tutorRepository.delete(tutor);
    }
}