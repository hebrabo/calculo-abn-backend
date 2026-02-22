package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.mapper.ProgresoMapper;
import com.abn.backend.model.InfantPerfil;
import com.abn.backend.model.ProgresoJuego;
import com.abn.backend.repository.InfantPerfilRepository;
import com.abn.backend.repository.ProgresoJuegoRepository;
import com.abn.backend.service.ProgresoJuegoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProgresoJuegoServiceImpl implements ProgresoJuegoService {

    private final ProgresoJuegoRepository progresoRepository;
    private final InfantPerfilRepository infantRepository;
    private final ProgresoMapper progresoMapper;

    @Override
    public List<ProgresoResponseDTO> obtenerProgresosPorInfante(Long infantId) {
        // Busquem l'infant i obtenim la seua llista (Relació 1:N)
        InfantPerfil infant = infantRepository.findById(infantId)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado"));

        return infant.getProgresos().stream()
                .map(progresoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProgresoResponseDTO actualizarProgreso(Long id, ProgresoUpdateDTO dto) {
        // Mètode estàndard: Busquem el registre per ID
        ProgresoJuego existente = progresoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Progreso no encontrado con id: " + id));

        // 1. Sincronització de l'estat del joc (Sincronització Offline Unity)
        existente.setEstrellasGanadas(dto.getEstrellasGanadas());
        existente.setDesbloqueado(dto.isDesbloqueado());

        // 2. Registre de l'analítica per a nens de 3 a 5 anys
        existente.setTiempoSegundos(dto.getTiempoSegundos());
        existente.setIntentosFallidos(dto.getIntentosFallidos());

        // 3. Persistència
        return progresoMapper.toDto(progresoRepository.save(existente));
    }

    @Override
    public boolean puedeDesbloquearJuego(Long infantId, int juegoId) {

        InfantPerfil infant = infantRepository.findById(infantId)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado"));

        if (juegoId == 1303) {
            int estrellasJuego1 = infant.getProgresos().stream()
                    .filter(p -> p.getIdJuego() == 1301L)
                    .findFirst()
                    .map(ProgresoJuego::getEstrellasGanadas).orElse(0);

            int estrellasJuego2 = infant.getProgresos().stream()
                    .filter(p -> p.getIdJuego() == 1302L)
                    .findFirst()
                    .map(ProgresoJuego::getEstrellasGanadas).orElse(0);

            // Requisit pedagògic ABN: 3 estrelles en ambdós jocs previs
            return estrellasJuego1 >= 3 && estrellasJuego2 >= 3;
        }

        return true;
    }
}