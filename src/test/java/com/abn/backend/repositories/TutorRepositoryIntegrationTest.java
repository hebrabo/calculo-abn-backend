package com.abn.backend.repositories;

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
        // 1. GIVEN: Preparem el Tutor i la seva Configuració (Relació 1:1)
        TutorPerfil tutor = new TutorPerfil();
        tutor.setEmail("profe_test@abn.com");

        ConfiguracionTutor config = new ConfiguracionTutor();
        config.setIdioma("Castellano");
        config.setMusicaActivada(true);
        config.setVolumenEfectos(50);

        // Sincronitzem la relació
        tutor.setConfiguracion(config);

        // Persistim el tutor (i per cascada ALL, la configuració)
        TutorPerfil guardado = tutorRepository.save(tutor);
        Long idConfig = guardado.getConfiguracion().getId();

        // 2. WHEN: Executem l'acció d'esborrat
        tutorRepository.delete(guardado);

        // Forcem el buidat a la BD en memòria (H2) i netegem la memòria cau
        tutorRepository.flush();
        entityManager.clear();

        // 3. THEN: Verificacions de persistència
        // Comprovem que el tutor ja no existeix
        assertFalse(tutorRepository.findById(guardado.getId()).isPresent());

        // Comprovem que la configuració s'ha esborrat per cascada (usant l'EntityManager)
        ConfiguracionTutor configEnBD = entityManager.find(ConfiguracionTutor.class, idConfig);
        assertNull(configEnBD, "La configuración debería haber sido borrada por la cascada de JPA");
    }
}