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
    public List<InfantResponseDTO> obtenerTodosLosInfantes() {
        return infantRepository.findAll().stream()
                .map(infantMapper::toDto)
                .toList();
    }

    @Override
    public InfantResponseDTO obtenerInfantePorId(Long id) {
        // Estil Profe: orElseThrow amb NoSuchElementException
        InfantPerfil infant = infantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado con id: " + id));
        return infantMapper.toDto(infant);
    }

    @Override
    @Transactional
    public InfantResponseDTO crearInfante(InfantCreateDTO dto) {
        // Busquem el tutor (Relació 1:N)
        TutorPerfil tutor = tutorRepository.findById(dto.getTutorId())
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado"));

        InfantPerfil infant = infantMapper.toEntity(dto);
        infant.setTutor(tutor);

        // Lògica de Negoci: Generació automàtica dels 100 jocs ABN
        List<ProgresoJuego> progresos = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            ProgresoJuego p = new ProgresoJuego();
            p.setIdJuego((long) i);
            p.setNombreJuego("Juego " + i);
            p.setInfante(infant);
            p.setEstrellasGanadas(0);
            p.setTiempoSegundos(0.0);
            p.setIntentosFallidos(0);

            // Filtre per edat (3-5 anys)
            if (dto.getEdad() == 3 && i <= 33) p.setDesbloqueado(true);
            else if (dto.getEdad() == 4 && i <= 66) p.setDesbloqueado(true);
            else if (dto.getEdad() >= 5) p.setDesbloqueado(true);
            else p.setDesbloqueado(false);

            progresos.add(p);
        }
        infant.setProgresos(progresos);

        // Usem save() com el profe. Hibernate gestionarà la cascada cap als 100 progressos.
        return infantMapper.toDto(infantRepository.save(infant));
    }

    @Override
    @Transactional
    public InfantResponseDTO actualizarInfante(Long id, InfantUpdateDTO dto) {
        InfantPerfil existente = infantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Infante no encontrado"));

        // Estil Profe: Actualitzem sobre l'objecte recuperat
        infantMapper.updateInfantFromDto(dto, existente);
        return infantMapper.toDto(infantRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminarInfante(Long id) {
        // Verifiquem primer si existeix (Punt 5 del PDF: Lògica de control)
        InfantPerfil infant = infantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Infante no encontrado"));

        infantRepository.delete(infant);
    }
}