package com.abn.backend.service;

import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import java.util.List;

/**
 * INTERFÍCIE DE SERVEI - GESTIÓ DE TUTORS
 * Aquesta capa defineix el contracte de negoci per al perfil de tutor.
 * És el component que orquestra la relació entre els tutors i els seus infants.
 */
public interface TutorPerfilService {

    /**
     * LLISTAT GLOBAL:
     * Retorna tots els tutors del sistema. Útil per a tasques de manteniment.
     */
    List<TutorResponseDTO> obtenerTodosLosTutores();

    /**
     * CERCA PER IDENTIFICADOR:
     * Recupera el perfil del tutor, incloent-hi la seva configuració niuada.
     */
    TutorResponseDTO obtenerTutorPorId(Long id);

    /**
     * REGISTRE (POST):
     * Crea un nou tutor a partir de les dades de registre d'Unity.
     */
    TutorResponseDTO crearTutor(TutorCreateDTO dto);

    /**
     * ACTUALITZACIÓ (PUT):
     * Modifica dades del tutor o els seus ajustos de configuració global.
     */
    TutorResponseDTO actualizarTutor(Long id, TutorUpdateDTO dto);

    /**
     * ELIMINACIÓ (DELETE):
     * Esborra el tutor i, per cascada, tots els perfils d'infant i progressos associats.
     */
    void eliminarTutor(Long id);

    /**
     * RECUPERACIÓ DE DEPENDÈNCIES (Relació 1:N):
     * Aquest mètode és clau per a l'arquitectura. En lloc de carregar tots els infants
     * dins de l'objecte Tutor (que podria ser massa pesat), definim un punt d'accés
     * específic per obtenir només els nens vinculats a aquest tutor concret.
     */
    List<InfantResponseDTO> obtenerInfantesPorTutor(Long id);
}