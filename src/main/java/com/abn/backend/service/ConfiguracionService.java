package com.abn.backend.service;

import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;

/**
 * INTERFÍCIE DE SERVEI - CAPA DE LÒGICA DE NEGOCI
 * Defineix les operacions permeses sobre la configuració del tutor.
 * Actua com un "contracte" que garanteix el desacoblament entre el Controlador i la Persistència.
 */
public interface ConfiguracionService {

    /**
     * ACTUALITZACIÓ DE PREFERÈNCIES:
     * Rep la ID del tutor (per seguretat i traçabilitat) i les noves dades (DTO).
     * @return El DTO de resposta amb les dades actualitzades per refrescar la UI de Unity.
     */
    ConfiguracionResponseDTO actualizarConfiguracion(Long tutorId, ConfiguracionUpdateDTO dto);

    /**
     * CONSULTA DE CONFIGURACIÓ:
     * Recupera els ajustos (volum, música, idioma) vinculats a un tutor concret.
     * Vital per a l'arrencada de l'app (Splash Screen) a Unity.
     */
    ConfiguracionResponseDTO obtenerConfiguracionPorTutor(Long tutorId);
}