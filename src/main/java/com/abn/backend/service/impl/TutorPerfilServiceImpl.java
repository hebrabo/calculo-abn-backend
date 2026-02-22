package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.mapper.TutorPerfilMapper;
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
        return tutorRepository.findAll()
                .stream()
                .map(tutorMapper::toDto)
                .toList();
    }

    @Override
    public TutorResponseDTO obtenerTutorPorId(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tutor con id " + id + " no encontrado"));
        return tutorMapper.toDto(tutor);
    }

    @Override
    @Transactional
    public TutorResponseDTO crearTutor(TutorCreateDTO dto) {
        // Seguim l'estil del profe: El Mapper s'encarrega de tot.
        // Si la lògica de negoci exigeix una configuració inicial,
        // aquesta hauria d'estar en el constructor de l'entitat o en el Mapper.
        TutorPerfil tutor = tutorMapper.toEntity(dto);

        // Persistim el tutor (automàticament persistirà la configuració per la cascada)
        TutorPerfil tutorGuardado = tutorRepository.save(tutor);
        return tutorMapper.toDto(tutorGuardado);
    }

    @Override
    @Transactional
    public TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto) {
        // 1. Buscar el tutor real en la BBDD por Id
        TutorPerfil existente = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // 2. Actualizar campos usando el Mapper (igual que l'exemple del profe)
        tutorMapper.updateTutorFromDto(dto, existente);

        // 3. Guardar el tutor con su estado preservado
        return tutorMapper.toDto(tutorRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminarTutor(Long id) {
        TutorPerfil tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con id: " + id));

        // Verificació d'infants associats (Lògica similar a les 'cuentas' del profe)
        if (tutor.getInfantes() != null && !tutor.getInfantes().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un tutor con infantes asociados.");
        }

        // Eliminació en cascada de la configuració (orphanRemoval = true)
        tutorRepository.delete(tutor);
    }
}