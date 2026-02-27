package com.abn.backend.dto.response;

import lombok.Data;

/**
 * DTO DE RESPOSTA DE TELEMETRIA
 * Aquest objecte és el que Unity rep quan l'app s'arrenca (Splash)
 * o després de desar una partida. És la veritat absoluta que resideix al núvol.
 */
@Data // Lombok: Genera automàticament els mètodes necessaris per a la serialització.
// Jackson (la llibreria de Spring) els fa servir per convertir aquest objecte en el JSON que Unity entén.
public class ProgresoResponseDTO {

    /**
     * ID REMOTA: Clau primària (PK) a la base de dades PostgreSQL (AWS RDS).
     * És el valor més important per a la sincronització: Unity el guardarà
     * al camp 'id_remot' de la seva SQLite per saber que aquest registre ja existeix al núvol.
     */
    private Long id;

    /**
     * CODI PEDAGÒGIC: Identificador de l'activitat dins del mètode ABN (Ex: 1301).
     * Permet que Unity sàpiga a quin dels 100 jocs correspon aquest progrés.
     */
    private long idJuego;

    private String nombreJuego;

    /**
     * ESTAT DEL JOC: Indica si l'infant té permís per jugar a aquesta activitat.
     * Serveix per a la lògica de desbloqueig seqüencial.
     */
    private boolean desbloqueado;

    /**
     * RENDIMENT: El resultat màxim obtingut (estrelles).
     */
    private int estrellasGanadas;

    /**
     * ANALÍTICA PEDAGÒGICA: Dades per al seguiment del tutor/mestre.
     * tempsSegundos i intentosFallidos permeten fer informes sobre la corba d'aprenentatge.
     */
    private double tiempoSegundos;
    private int intentosFallidos;
}