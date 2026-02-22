package com.abn.backend.services;

import com.abn.backend.mapper.TutorPerfilMapper;
import com.abn.backend.model.InfantPerfil;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import com.abn.backend.service.impl.TutorPerfilServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Inicializa Mockito sin cargar Spring
class TutorPerfilServiceTest {

    @Mock // Crea un objeto simulado vacío
    private TutorPerfilRepository tutorRepository;

    @Mock
    private TutorPerfilMapper tutorMapper;

    @InjectMocks // Crea la instancia e inyecta los mocks
    private TutorPerfilServiceImpl tutorService;

    private TutorPerfil tutorTest;

    @BeforeEach
    void setUp() {
        // Inicialització
        tutorTest = new TutorPerfil();
        tutorTest.setId(1L);
        tutorTest.setEmail("test@abn.com");
        tutorTest.setInfantes(new ArrayList<>()); // Inicialitzem la llista de nens buida
    }

    @Test
    void eliminarTutor_Exito_SinInfantes() {
        // GIVEN (Preparació)
        Long id = 1L;
        // Simularem que el tutor existeix i no té nens (llista buida del setUp)
        when(tutorRepository.findById(id)).thenReturn(Optional.of(tutorTest));

        // WHEN (Execució)
        tutorService.eliminarTutor(id);

        // THEN (Verificació)
        // Verifiquem que es va cridar al mètode delete exactament una vegada
        verify(tutorRepository, times(1)).delete(tutorTest);
    }

    @Test
    void eliminarTutor_Error_ConInfantes() {
        // GIVEN (Preparació)
        Long id = 1L;
        // Afegim un infant per disparar la restricció lògica
        tutorTest.getInfantes().add(new InfantPerfil());
        when(tutorRepository.findById(id)).thenReturn(Optional.of(tutorTest));

        // WHEN & THEN (Execució i Verificació de l'excepció)
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            tutorService.eliminarTutor(id);
        });

        // Verifiquem que el missatge sigui l'esperat
        assertEquals("No se puede eliminar un tutor con infantes asociados.", exception.getMessage());

        // Verifiquem que MAI es va arribar a cridar al delete del repositori
        verify(tutorRepository, never()).delete(any(TutorPerfil.class));
    }
}