package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * ENTITAT ARREL - EL PATRÓ DELS RECURSOS
 * Aquesta classe representa el Tutor i és l'amo de les relacions 1:1 i 1:N.
 * Totes les operacions de persistència que es facin aquí afectaran als fills.
 */
@Data // Lombok: Genera Getters, Setters, Equals i HashCode.
@Entity // Defineix la classe com a entitat JPA.
@Table(name = "tutores_perfil") // Mapeig a la taula física de la base de dades.
public class TutorPerfil {

    /**
     * CLAU PRIMÀRIA (PK):
     * @Id i @GeneratedValue: Gestió automàtica d'identificadors per la DB.
     * @Column(name = "id_tutor"): Mapeig explícit per coincidir amb l'esquema SQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tutor")
    private Long id;

    /**
     * @Column(nullable = false, unique = true):
     * Implementa restriccions de seguretat a nivell de base de dades per
     * evitar duplicats i valors nuls en el correu electrònic.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * RELACIÓ ONE-TO-ONE (Bi-direccional):
     * mappedBy: Indica que l'amo de la FK és l'entitat 'ConfiguracionTutor'.
     * CascadeType.ALL: Si guardem/esborrem el tutor, la configuració es guarda/esborra amb ell.
     * orphanRemoval: Si el tutor es queda sense configuració, JPA l'elimina de la DB.
     */
    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private ConfiguracionTutor configuracion;

    /**
     * RELACIÓ ONE-TO-MANY (1:N):
     * Representa la llista d'infants a càrrec del tutor.
     * Mantenim la persistència en cascada per facilitar la gestió de dades.
     */
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfantPerfil> infantes;

    /**
     * MÈTODE DE CONVENIÈNCIA (Helper method):
     * Aquest setter és vital per a la bi-direccionalitat. Assegura que
     * quan assignem una configuració a un tutor, la configuració també
     * "sàpiga" qui és el seu tutor, evitant inconsistències en la memòria.
     */
    public void setConfiguracion(ConfiguracionTutor configuracion) {
        this.configuracion = configuracion;
        if (configuracion != null) {
            configuracion.setTutor(this);
        }
    }
}