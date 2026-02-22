package com.abn.backend.repositories;

import com.abn.backend.model.ConfiguracionTutor;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Testcontainers
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.test.database.replace=none"
})
public class TutorRepositoryIntegrationTest {

    // Contenidor Postgres
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("abn_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TutorPerfilRepository tutorRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void eliminarTutor_DebeEliminarConfiguracionEnCascada_RealDB() {
        // GIVEN: Relació 1:1
        TutorPerfil tutor = new TutorPerfil();
        tutor.setEmail("tutor_integracion@abn.com");

        ConfiguracionTutor config = new ConfiguracionTutor();
        config.setIdioma("Català");
        config.setMusicaActivada(true);
        config.setVolumenEfectos(80);
        tutor.setConfiguracion(config);

        TutorPerfil guardado = tutorRepository.save(tutor);
        Long idConfig = guardado.getConfiguracion().getId();

        // WHEN: Esborrat real en el contenidor Postgres
        tutorRepository.delete(guardado);
        tutorRepository.flush(); // Asegura el borrado en Docker
        entityManager.clear();   // Limpia la memoria para forzar lectura de disco

        // THEN: Verificació de persistència real
        assertFalse(tutorRepository.findById(guardado.getId()).isPresent());
        assertNull(entityManager.find(ConfiguracionTutor.class, idConfig),
                "La configuració hauria d'haver estat esborrada per la cascada en Postgres");
    }
}