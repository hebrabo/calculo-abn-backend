package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

/**
 * ENTITAT JPA - MAPEIG DE PERSISTÈNCIA
 * Aquesta classe representa la taula de configuracions a la base de dades.
 * Hibernate utilitzarà aquesta metadada per generar el SQL automàticament.
 */
@Entity // Marca la classe com una entitat gestionada per JPA
@Data   // Lombok: Genera Getters, Setters (resolent el "cannot resolve" del Mapper),
// equals, hashCode i constructor.
@Table(name = "configuraciones_tutor") // Especifica el nom real de la taula a PostgreSQL
public class ConfiguracionTutor {

    /**
     * CLAU PRIMÀRIA (PK):
     * @Id: Defineix l'identificador únic de la fila.
     * @GeneratedValue: Indica que la base de dades s'encarrega d'incrementar
     * el valor (estratègia SERIAL a Postgres o IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Camps de configuració que es mapegen directament a columnes de la taula
    private boolean musicaActivada;
    private int volumenEfectos;
    private String idioma;

    /**
     * RELACIÓ ONE-TO-ONE (1:1):
     * Cada configuració pertany a un únic tutor.
     * @JoinColumn: Defineix la Clau Estrangera (FK) en aquesta taula.
     * @ToString.Exclude: VITAL. Evita que el mètode toString() cridi al Tutor
     * i el Tutor cridi a la Configuració, provocant un error de 'StackOverflow'
     * per recursivitat infinita.
     */
    @OneToOne
    @JoinColumn(name = "id_tutor")
    @ToString.Exclude
    private TutorPerfil tutor;
}