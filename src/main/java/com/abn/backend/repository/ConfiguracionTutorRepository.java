package com.abn.backend.repository;

import com.abn.backend.model.ConfiguracionTutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITORI DE CONFIGURACIÓ
 * Aquesta interfície és la capa d'accés a dades (DAO).
 * Spring Data JPA genera automàticament la implementació en temps d'execució.
 */
@Repository // Tot i que és opcional en estendre de JpaRepository, ajuda a la claredat i a la traducció d'excepcions de persistència.
public interface ConfiguracionTutorRepository extends JpaRepository<ConfiguracionTutor, Long> {

    /**
     * A l'heretar de JpaRepository, ja tenim disponibles sense programar res:
     * - save(): Per a INSERT i UPDATE.
     * - findById(): Per a SELECT por PK.
     * - findAll(): Per a llistats.
     * - delete(): Per a DELETE.
     * * El primer paràmetre <ConfiguracionTutor> és l'Entitat que gestionem.
     * El segon paràmetre <Long> és el tipus de dada de la seva Clau Primària (ID).
     */
}