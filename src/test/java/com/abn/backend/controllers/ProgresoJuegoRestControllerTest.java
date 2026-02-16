package com.abn.backend.controllers;

import com.abn.backend.controller.ProgresoJuegoRestController;
import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.service.ProgresoJuegoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de la Capa Controlador.
 * Verifica que els Endpoints responen correctament al format JSON d'Unity.
 */
@WebMvcTest(ProgresoJuegoRestController.class)
public class ProgresoJuegoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProgresoJuegoService progresoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProgresoUpdateDTO progresoUpdateDTO;
    private ProgresoResponseDTO progresoResponseDTO;

    @BeforeEach
    void setUp() {
        // 1. Preparem el DTO d'entrada (el que enviaria l'APIManager d'Unity)
        progresoUpdateDTO = new ProgresoUpdateDTO();
        progresoUpdateDTO.setEstrellasGanadas(3);
        progresoUpdateDTO.setDesbloqueado(true);
        progresoUpdateDTO.setTiempoSegundos(45.5);
        progresoUpdateDTO.setIntentosFallidos(2);

        // 2. Preparem la resposta simulada (el que Unity espera rebre per sincronitzar)
        progresoResponseDTO = new ProgresoResponseDTO();
        progresoResponseDTO.setId(1L);
        progresoResponseDTO.setIdJuego(1301L);
        progresoResponseDTO.setNombreJuego("Juego 1");
        progresoResponseDTO.setEstrellasGanadas(3);
        progresoResponseDTO.setTiempoSegundos(45.5);
        progresoResponseDTO.setIntentosFallidos(2);
    }

    @Test
    @DisplayName("PUT /api/progresos/1 - Sincronització d'analítica des d'Unity")
    void actualizarProgreso_Exito() throws Exception {
        // GIVEN
        when(progresoService.actualizarProgreso(eq(1L), any(ProgresoUpdateDTO.class)))
                .thenReturn(progresoResponseDTO);

        // WHEN & THEN
        mockMvc.perform(put("/api/progresos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(progresoUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estrellasGanadas").value(3))
                .andExpect(jsonPath("$.tiempoSegundos").value(45.5))
                .andExpect(jsonPath("$.idJuego").value(1301));

        verify(progresoService).actualizarProgreso(eq(1L), any(ProgresoUpdateDTO.class));
    }

    @Test
    @DisplayName("GET /api/progresos/puede-jugar/22/1303 - Validació de desbloqueig ABN")
    void comprobarAcceso_Exito() throws Exception {
        // GIVEN
        when(progresoService.puedeDesbloquearJuego(22L, 1303)).thenReturn(true);

        // WHEN & THEN
        mockMvc.perform(get("/api/progresos/puede-jugar/22/1303"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}