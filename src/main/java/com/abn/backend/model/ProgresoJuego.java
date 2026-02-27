package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ENTITAT DE TELEMETRIA - SEGUIMENT DEL RENDIMENT
 * Aquesta classe mapeja la taula 'progresos_juego', on es registra
 * l'activitat de cada infant en cadascun dels 100 jocs del mètode.
 */
@Entity
@Data // Lombok: Genera Getters, Setters, equals i hashCode automàticament.
@Table(name = "progresos_juego")
public class ProgresoJuego {

    /**
     * IDENTIFICADOR ÚNIC (PK):
     * @GeneratedValue amb IDENTITY és l'estratègia òptima per a PostgreSQL (AWS RDS),
     * delegant l'autoincrement a la columna de la base de dades.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador pedagògic del joc (Ex: 101, 102...)
    private long idJuego;

    private String nombreJuego;

    // Indica si l'infant ha superat els requisits per jugar a aquest nivell
    private boolean desbloqueado;

    // Recompensa obtinguda (0 a 3 estrelles)
    private int estrellasGanadas;

    /**
     * COLUMNES D'ANALÍTICA:
     * tiempoSegundos: Utilitzem 'double' per recollir la precisió del cronòmetre de Unity.
     * intentosFallidos: Dada crítica per detectar dificultats d'aprenentatge.
     */
    private double tiempoSegundos;
    private int intentosFallidos;

    /**
     * RELACIÓ MANY-TO-ONE (N:1):
     * Molts registres de progrés pertanyen a un sol infant.
     * @JoinColumn: Defineix la Foreign Key (FK) 'infante_id'.
     * nullable = false: Garanteix la integritat referencial; no pot existir un progrés sense infant.
     * @JsonIgnore: Evita la recursivitat infinita en cas que es serialitzi l'entitat directament.
     */
    @ManyToOne
    @JoinColumn(name = "infante_id", nullable = false)
    @JsonIgnore
    private InfantPerfil infante;
}