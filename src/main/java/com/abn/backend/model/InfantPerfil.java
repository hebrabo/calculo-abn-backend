package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * ENTITAT INFANT
 * Aquesta classe mapeja la taula 'infantes_perfil' i gestiona la
 * integritat referencial entre tutors i progressos.
 */
@Entity
@Data // Lombok genera getters, setters i el mètode toString automàticament.
@Table(name = "infantes_perfil")
public class InfantPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String avatar;
    private int edad;

    /**
     * RELACIÓ MANY-TO-ONE (N:1):
     * Molts infants estan vinculats a un únic Tutor (el "pare" del recurs).
     * @JoinColumn: Especifica la clau estrangera (FK) a la base de dades.
     * nullable = false: Garanteix que no hi hagi nens "orfes" a la DB,
     * protegint la integritat de les dades.
     */
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private TutorPerfil tutor;

    /**
     * RELACIÓ ONE-TO-MANY (1:N):
     * Un infant té tot un historial de progressos (els 100 jocs ABN).
     * mappedBy: Indica que l'amo de la relació és el camp 'infante' a la classe ProgresoJuego.
     * * PERSISTÈNCIA EN CASCADA:
     * CascadeType.ALL: Si esborrem un infant, s'esborren automàticament tots els seus progressos.
     * orphanRemoval = true: Si traiem un progrés de la llista, JPA l'eliminarà físicament de la DB.
     */
    @OneToMany(mappedBy = "infante", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ProgresoJuego> progresos;
}