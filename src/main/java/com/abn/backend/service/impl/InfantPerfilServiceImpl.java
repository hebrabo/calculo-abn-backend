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

/**
 * IMPLEMENTACIÓ DE LA LÒGICA DE NEGOCI D'INFANTS
 * Aquesta classe actua com el "cervell" que gestiona com els nens interactuen
 * amb el sistema i com s'inicialitzen els seus 100 jocs.
 */
@Service // Indica a Spring que aquesta classe conté la lògica de negoci (Bean de servei).
@RequiredArgsConstructor // Genera automàticament el constructor per a la Injecció de Dependències.
public class InfantPerfilServiceImpl implements InfantPerfilService {

    private final InfantPerfilRepository infantRepository;
    private final TutorPerfilRepository tutorRepository;
    private final InfantPerfilMapper infantMapper;

    /**
     * RECUPERACIÓ MASIVA:
     * Utilitzem l'API Stream de Java per mapejar de forma funcional totes les entitats a DTOs.
     * readOnly = true optimitza la consulta a nivell de base de dades (Hibernate no vigila canvis).
     */
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
        InfantPerfil infant = infantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado con id: " + id));
        return infantMapper.toDto(infant);
    }

    /**
     * CREACIÓ I INICIALITZACIÓ PEDAGÒGICA:
     * Aquest mètode és vital. No només crea un nen, sinó que li genera el seu
     * propi "tauler de joc" personalitzat de 100 activitats.
     */
    @Override
    @Transactional // Garantia ACID: O es crea tot (nen + 100 jocs) o no es crea res.
    public InfantResponseDTO crearInfante(InfantCreateDTO dto) {
        // 1. Validació d'Integritat: Verifiquem que el Tutor (Foreign Key) existeix a AWS RDS.
        TutorPerfil tutor = tutorRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado con ID: " + dto.getTutorId()));

        // 2. Transformació: Passem de les dades del formulari (DTO) al model de dades (Entity).
        InfantPerfil infant = infantMapper.toEntity(dto);
        infant.setTutor(tutor);

        // 3. GENERACIÓ DELS 100 JOCS (Lògica ABN):
        // Inicialitzem l'estat de cada joc per a aquest infant concret.
        List<ProgresoJuego> progresos = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            ProgresoJuego p = new ProgresoJuego();
            p.setIdJuego((long) i);
            p.setNombreJuego("Juego " + i);
            p.setInfante(infant); // Mantenim la relació bi-direccional.
            p.setEstrellasGanadas(0);
            p.setTiempoSegundos(0.0);
            p.setIntentosFallidos(0);

            // REGLA DE NEGOCI: Desbloqueig progressiu segons l'edat (3, 4 o 5 anys).
            if (dto.getEdad() == 3 && i <= 33) {
                p.setDesbloqueado(true);
            } else if (dto.getEdad() == 4 && i <= 66) {
                p.setDesbloqueado(true);
            } else if (dto.getEdad() >= 5) {
                p.setDesbloqueado(true); // Els de 5 anys tenen accés a tot l'itinerari.
            } else {
                p.setDesbloqueado(false);
            }

            progresos.add(p);
        }
        infant.setProgresos(progresos);

        // 4. PERSISTÈNCIA EN CASCADA:
        // Gràcies a CascadeType.ALL al model, en desar l'infant es guarden
        // automàticament els 100 objectes de progrés vinculats.
        InfantPerfil guardado = infantRepository.save(infant);

        return infantMapper.toDto(guardado);
    }

    /**
     * ACTUALITZACIÓ:
     * El Mapper s'encarrega de traspassar només els camps permesos del DTO a l'entitat persistent.
     */
    @Override
    @Transactional
    public InfantResponseDTO actualizarInfante(Long id, InfantUpdateDTO dto) {
        InfantPerfil existente = infantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Infante no encontrado con ID: " + id));

        infantMapper.updateInfantFromDto(dto, existente);

        return infantMapper.toDto(infantRepository.save(existente));
    }

    /**
     * ELIMINACIÓ:
     * A l'esborrar l'infant, la base de dades neteja automàticament els seus
     * progressos (orphanRemoval = true).
     */
    @Override
    @Transactional
    public void eliminarInfante(Long id) {
        InfantPerfil infant = infantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Infante no encontrado con ID: " + id));

        infantRepository.delete(infant);
    }
}