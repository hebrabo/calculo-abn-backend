package com.abn.backend.services;

import com.abn.backend.model.InfantPerfil;
import com.abn.backend.model.ProgresoJuego;
import com.abn.backend.repository.InfantPerfilRepository;
import com.abn.backend.service.impl.ProgresoJuegoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgresoJuegoServiceTest {

    @Mock
    private InfantPerfilRepository infantRepository;

    @InjectMocks
    private ProgresoJuegoServiceImpl progresoJuegoService;

    private InfantPerfil infantTest;

    @BeforeEach
    void setUp() {
        infantTest = new InfantPerfil();
        infantTest.setId(22L);
        infantTest.setNombre("Carmen");
        infantTest.setProgresos(new ArrayList<>());
    }

    @Test
    void puedeDesbloquearJuego_Exito_RequisitosCumplidos() {
        // GIVEN (Preparació)
        Long id = 22L;

        // Simulem dades
        ProgresoJuego p1 = new ProgresoJuego();
        p1.setIdJuego(1301L);
        p1.setEstrellasGanadas(3);

        ProgresoJuego p2 = new ProgresoJuego();
        p2.setIdJuego(1302L);
        p2.setEstrellasGanadas(3);

        // Afegim a la llista inicialitzada al setUp
        infantTest.getProgresos().add(p1);
        infantTest.getProgresos().add(p2);

        when(infantRepository.findById(id)).thenReturn(Optional.of(infantTest));

        // WHEN (Execució)
        boolean resultado = progresoJuegoService.puedeDesbloquearJuego(id, 1303);

        // THEN (Verificació)
        assertEquals(true, resultado);
        // Verifiquem que s'ha cridat al repo exactament una vegada
        verify(infantRepository, times(1)).findById(id);
    }

    @Test
    void puedeDesbloquearJuego_Error_PuntuacionBaja() {
        // GIVEN (Preparació)
        Long id = 22L;

        ProgresoJuego p1 = new ProgresoJuego();
        p1.setIdJuego(1301L);
        p1.setEstrellasGanadas(3);

        ProgresoJuego p2 = new ProgresoJuego();
        p2.setIdJuego(1302L);
        p2.setEstrellasGanadas(1); // No arriba al mínim de 3 estrelles

        infantTest.getProgresos().add(p1);
        infantTest.getProgresos().add(p2);

        when(infantRepository.findById(id)).thenReturn(Optional.of(infantTest));

        // WHEN (Execució)
        boolean resultado = progresoJuegoService.puedeDesbloquearJuego(id, 1303);

        // THEN (Verificació)
        assertEquals(false, resultado);
        verify(infantRepository, times(1)).findById(id);
    }
}