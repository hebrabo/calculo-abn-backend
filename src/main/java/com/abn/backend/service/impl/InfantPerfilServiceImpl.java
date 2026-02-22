package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.mapper.InfantPerfilMapper;
import com.abn.backend.model.InfantPerfil;
import com.abn.backend.model.ProgresoJuego;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.InfantPerfilRepository;
import com.abn.backend.repository.TutorPerfilRepository;
import com.abn.backend.service.InfantPerfilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InfantPerfilServiceImpl implements InfantPerfilService {

    private final InfantPerfilRepository infantRepository;
    private final TutorPerfilRepository tutorRepository;
    private final InfantPerfilMapper infantMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InfantResponseDTO> obtenerTodosLosInfantes() {
        return infantRepository.findAll().stream()
                .map(infantMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InfantResponseDTO obtenerInfantePorId(Long id) {
        // Estil Profe: orElseThrow amb NoSuchElementException per a IDs no trobats
        InfantPerfil infant = infantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado con id: " + id));
        return infantMapper.toDto(infant);
    }

    @Override
    @Transactional
    public InfantResponseDTO crearInfante(InfantCreateDTO dto) {
        // 1. Busquem el tutor (Relació 1:N). Si no existeix, llancem excepció d'integritat.
        TutorPerfil tutor = tutorRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con ID: " + dto.getTutorId()));

        // 2. Mapegem el DTO a l'entitat base
        InfantPerfil infant = infantMapper.toEntity(dto);
        infant.setTutor(tutor);

        // 3. Lògica de Negoci ABN: Generació automàtica dels 100 jocs
        List<ProgresoJuego> progresos = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            ProgresoJuego p = new ProgresoJuego();
            p.setIdJuego((long) i);
            p.setNombreJuego("Juego " + i);
            p.setInfante(infant);
            p.setEstrellasGanadas(0);
            p.setTiempoSegundos(0.0);
            p.setIntentosFallidos(0);

            // Filtre de desbloqueig per edat (3-5 anys)
            if (dto.getEdad() == 3 && i <= 33) {
                p.setDesbloqueado(true);
            } else if (dto.getEdad() == 4 && i <= 66) {
                p.setDesbloqueado(true);
            } else if (dto.getEdad() >= 5) {
                p.setDesbloqueado(true);
            } else {
                p.setDesbloqueado(false);
            }

            progresos.add(p);
        }
        infant.setProgresos(progresos);

        // 4. Guardem l'infant. Hibernate persistirà els 100 progressos gràcies al CascadeType.ALL
        InfantPerfil guardado = infantRepository.save(infant);

        return infantMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public InfantResponseDTO actualizarInfante(Long id, InfantUpdateDTO dto) {
        InfantPerfil existente = infantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Infante no encontrado con ID: " + id));

        // Estil Profe: Actualitzem els camps de l'objecte gestionat pel context de persistència
        infantMapper.updateInfantFromDto(dto, existente);

        return infantMapper.toDto(infantRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminarInfante(Long id) {
        // Verificació prèvia d'existència (Lògica de control de flux)
        InfantPerfil infant = infantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Infante no encontrado con ID: " + id));

        infantRepository.delete(infant);
    }
}