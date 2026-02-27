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

/**
 * IMPLEMENTACIÓ DEL SERVEI DE PROGRESSOS
 * Aquest component gestiona l'evolució de l'infant en el mètode ABN.
 * És el punt on les dades analítiques (temps, errors) es converteixen en informació útil.
 */
@Service // Registra la classe al context de Spring per a la Injecció de Dependències.
@RequiredArgsConstructor // Genera el constructor amb els camps 'final' (Injecció recomanada).
public class ProgresoJuegoServiceImpl implements ProgresoJuegoService {

    private final ProgresoJuegoRepository progresoRepository;
    private final InfantPerfilRepository infantRepository;
    private final ProgresoMapper progresoMapper;

    /**
     * CONSULTA DE PROGRESSOS PER INFANT
     * @param infantId ID del nen/a.
     * @return Llista de DTOs amb l'estat dels 100 jocs.
     * Fem servir el Repositori d'Infants per carregar els progressos
     * gràcies a la relació @OneToMany, aprofitant el graf d'objectes de JPA.
     */
    @Override
    public List<ProgresoResponseDTO> obtenerProgresosPorInfante(Long infantId) {
        InfantPerfil infant = infantRepository.findById(infantId)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado"));

        // Convertim la col·lecció d'entitats a DTOs per a Unity usant l'API Stream.
        return infant.getProgresos().stream()
                .map(progresoMapper::toDto)
                .toList();
    }

    /**
     * ACTUALITZACIÓ DE TELEMETRIA
     * @Transactional: Vital. Garanteix que l'actualització sigui atòmica.
     * Si el 'save' falla, es fa rollback i no perdem la consistència de la dada analítica.
     */
    @Override
    @Transactional
    public ProgresoResponseDTO actualizarProgreso(Long id, ProgresoUpdateDTO dto) {
        // Busquem el registre existent a la base de dades (PostgreSQL a AWS).
        ProgresoJuego existente = progresoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Progreso no encontrado con id: " + id));

        /**
         * 1. SINCRONITZACIÓ DE L'ESTAT:
         * Actualitzem els camps que Unity ha processat (estrelles i desbloqueig).
         */
        existente.setEstrellasGanadas(dto.getEstrellasGanadas());
        existente.setDesbloqueado(dto.isDesbloqueado());

        /**
         * 2. REGISTRE ANALÍTIC:
         * Aquestes dades són crucials per al tutor ABN. El temps i els intents
         * permeten avaluar si l'infant de 3-5 anys està assolint el concepte numèric.
         */
        existente.setTiempoSegundos(dto.getTiempoSegundos());
        existente.setIntentosFallidos(dto.getIntentosFallidos());

        // 3. PERSISTÈNCIA: El mètode save() actualitza el registre gràcies al Dirty Checking de JPA.
        return progresoMapper.toDto(progresoRepository.save(existente));
    }

    /**
     * LÒGICA PEDAGÒGICA DE DESBLOQUEIG
     * Aquí és on el servei va més enllà d'un simple CRUD.
     * Implementa les regles del mètode ABN per avançar entre nivells.
     */
    @Override
    public boolean puedeDesbloquearJuego(Long infantId, int juegoId) {

        InfantPerfil infant = infantRepository.findById(infantId)
                .orElseThrow(() -> new NoSuchElementException("Infante no encontrado"));

        // EXEMPLE DE REQUISIT: Per jugar al Joc 1303, cal dominar el 1301 i 1302.
        if (juegoId == 1303) {
            // Busquem l'estat dels jocs previs en la llista de progressos de l'infant.
            int estrellasJuego1 = obtenerEstrellas(infant, 1301L);
            int estrellasJuego2 = obtenerEstrellas(infant, 1302L);

            /**
             * REQUISIT ABN: Mínim 3 estrelles en ambdós jocs previs.
             * Lògica: $estrellas_{1301} \geq 3 \land estrellas_{1302} \geq 3$
             */
            return estrellasJuego1 >= 3 && estrellasJuego2 >= 3;
        }

        return true; // Per defecte, si no hi ha regla específica, es permet.
    }

    // Mètode auxiliar per mantenir el codi net (Refactoring)
    private int obtenerEstrellas(InfantPerfil infant, Long juegoId) {
        return infant.getProgresos().stream()
                .filter(p -> p.getIdJuego() == juegoId)
                .findFirst()
                .map(ProgresoJuego::getEstrellasGanadas)
                .orElse(0);
    }
}