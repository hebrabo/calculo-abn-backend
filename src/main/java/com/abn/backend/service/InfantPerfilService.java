package com.abn.backend.service;

import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import java.util.List;

/**
 * INTERFÍCIE DE SERVEI - GESTIÓ DE PERFILS INFANTILS
 * Defineix les regles de negoci per als infants que jugaran a les activitats ABN.
 * Separa la definició de les accions de la seva implementació técnica amb JPA.
 */
public interface InfantPerfilService {

    /**
     * LLISTAT TOTAL:
     * Útil per a panells d'administració o vista de selecció de perfils a Unity.
     * @return Una llista de DTOs per evitar exposar l'entitat @Entity directament.
     */
    List<InfantResponseDTO> obtenerTodosLosInfantes();

    /**
     * CERCA PER IDENTIFICADOR:
     * Recupera tota la telemetria i dades d'un infant concret.
     */
    InfantResponseDTO obtenerInfantePorId(Long id);

    /**
     * REGISTRE DE NOU INFANT:
     * Rep les dades bàsiques (nom, edat, avatar) i el vincula a un tutor existent.
     */
    InfantResponseDTO crearInfante(InfantCreateDTO dto);

    /**
     * ACTUALITZACIÓ DE PERFIL:
     * Permet modificar dades com l'edat o l'avatar sense canviar la ID ni el tutor.
     */
    InfantResponseDTO actualizarInfante(Long id, InfantUpdateDTO dto);

    /**
     * ELIMINACIÓ FÍSICA:
     * Esborra el perfil de la base de dades.
     * Això dispararà l'esborrat en cascada (CascadeType.ALL)
     * de tots els seus progressos de joc.
     */
    void eliminarInfante(Long id);
}