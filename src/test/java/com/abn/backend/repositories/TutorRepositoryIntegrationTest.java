package com.abn.backend.repositories; // 1. Corregim el paquet (sense la 's')

import com.abn.backend.model.ConfiguracionTutor;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TutorRepositoryIntegrationTest {

    @Autowired
    private TutorPerfilRepository tutorRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Borrat en Cascada - Eliminar Tutor ha d'esborrar la seua Configuració (1:1)")
    void eliminarTutor_DebeEliminarConfiguracionEnCascada() {
        // GIVEN
        TutorPerfil tutor = new TutorPerfil();
        tutor.setEmail("profe_test@abn.com");

        ConfiguracionTutor config = new ConfiguracionTutor();
        config.setIdioma("Castellano");
        config.setMusicaActivada(true);
        config.setVolumenEfectos(50);

        // Usem el mètode sincronitzat de l'entitat (el que té la lògica del profe)
        tutor.setConfiguracion(config);

        TutorPerfil guardado = tutorRepository.save(tutor);
        Long idConfig = guardado.getConfiguracion().getId();

        // WHEN
        tutorRepository.delete(guardado);
        tutorRepository.flush();
        entityManager.clear();

        // THEN
        assertFalse(tutorRepository.findById(guardado.getId()).isPresent());
        ConfiguracionTutor configEnBD = entityManager.find(ConfiguracionTutor.class, idConfig);
        assertNull(configEnBD, "La configuración debería haber sido borrada por la cascada");
    }
}