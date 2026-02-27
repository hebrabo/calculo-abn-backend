package com.abn.backend.service;

import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import java.util.List;

/**
 * INTERFÍCIE DE SERVEI - LÒGICA DE RENDIMENT I PROGRESSIÓ
 * Aquesta capa gestiona el flux de telemetria entre Unity i la base de dades.
 * És on resideix la intel·ligència per decidir l'avanç de l'infant en el mètode ABN.
 */
public interface ProgresoJuegoService {

    /**
     * SINCRONITZACIÓ DE DADES:
     * Recupera l'historial complet d'un infant.
     * És el mètode que Unity crida en el 'Splash Screen' per saber
     * quins jocs mostrar com a completats o bloquejats.
     */
    List<ProgresoResponseDTO> obtenerProgresosPorInfante(Long infantId);

    /**
     * ACTUALITZACIÓ DE TELEMETRIA:
     * Dessa els resultats d'una partida (estrelles, temps, errors).
     * @param id L'identificador del registre de progrés.
     * @param dto Les noves dades enviades des d'Unity.
     */
    ProgresoResponseDTO actualizarProgreso(Long id, ProgresoUpdateDTO dto);

    /**
     * LÒGICA PEDAGÒGICA (Regla de Negoci):
     * Comprova si un infant ha complert els requisits per accedir a un joc nou.
     * Per exemple: "No pots jugar al Joc 5 si no has obtingut almenys 2 estrelles al Joc 4".
     */
    boolean puedeDesbloquearJuego(Long infantId, int juegoId);
}