package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.mapper.ProgresoMapper;
import com.abn.backend.model.ProgresoJuego;
import com.abn.backend.repository.ProgresoJuegoRepository;
import com.abn.backend.service.ProgresoJuegoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgresoJuegoServiceImpl implements ProgresoJuegoService {

    private final ProgresoJuegoRepository progresoRepository;
    private final ProgresoMapper progresoMapper;

    @Override
    public List<ProgresoResponseDTO> obtenerProgresosPorInfante(Long infantId) {

        return progresoRepository.findAll().stream()
                .filter(p -> p.getInfante().getId().equals(infantId))
                .map(progresoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProgresoResponseDTO actualizarProgreso(Long id, ProgresoUpdateDTO dto) {
        ProgresoJuego existente = progresoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Progreso no encontrado"));

        // 1. Actualitzem les estrelles i l'estat
        existente.setEstrellasGanadas(dto.getEstrellasGanadas());
        existente.setDesbloqueado(dto.isDesbloqueado());

        // 2. AQUESTA ÉS LA PART NOVA: Analítica pedagògica
        existente.setTiempoSegundos(dto.getTiempoSegundos());
        existente.setIntentosFallidos(dto.getIntentosFallidos());

        // 3. Guardem i convertim a DTO per retornar-ho a Unity
        ProgresoJuego guardado = progresoRepository.save(existente);
        return progresoMapper.toDto(guardado);
    }
}