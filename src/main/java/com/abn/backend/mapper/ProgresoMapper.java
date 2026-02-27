package com.abn.backend.mapper;

import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.model.ProgresoJuego;
import org.springframework.stereotype.Component;

/**
 * MAPPER DE TELEMETRIA I RENDIMENT
 * Aquesta classe actua com a pont de sortida per a les dades analítiques.
 * Transforma l'entitat de persistència (PostgreSQL) en un objecte de transport (JSON).
 */
@Component // Registra la classe al contenidor d'Inversió de Control (IoC) de Spring.
public class ProgresoMapper {

    /**
     * ENTITAT -> DTO (Mapeig de Sortida)
     * Aquest mètode és fonamental per a la sincronització inicial (Splash Screen).
     * @param progreso L'objecte gestionat per JPA (Hibernate).
     * @return El DTO que serà serialitzat a JSON per Jackson.
     */
    public ProgresoResponseDTO toDto(ProgresoJuego progreso) {
        // Control de nul·litat: Evita NullPointerException en el flux de dades.
        if (progreso == null) return null;

        ProgresoResponseDTO dto = new ProgresoResponseDTO();

        /**
         * CLAU PRIMÀRIA (PK) vs CODI DE JOC:
         * .setId: És la ID única a la base de dades d'AWS. Unity la necessita
         * per fer peticions PUT posteriors (identificació del recurs).
         * .setIdJuego: És el codi pedagògic (Ex: 1301, 2405).
         */
        dto.setId(progreso.getId());
        dto.setIdJuego(progreso.getIdJuego());
        dto.setNombreJuego(progreso.getNombreJuego());

        /**
         * LÒGICA DE DESBLOQUEIG:
         * Aquesta dada permet que Unity sàpiga si ha de mostrar el cadenat
         * o permetre l'accés a l'activitat.
         */
        dto.setDesbloqueado(progreso.isDesbloqueado());
        dto.setEstrellasGanadas(progreso.getEstrellasGanadas());

        /**
         * ANALÍTICA DE TEMPS I PRECISIÓ:
         * Utilitzem 'double' per al temps per no perdre els decimals dels cronòmetres
         * de Unity, garantint una analítica mètrica precisa per al tutor.
         */
        dto.setTiempoSegundos(progreso.getTiempoSegundos());
        dto.setIntentosFallidos(progreso.getIntentosFallidos());

        return dto;
    }
}