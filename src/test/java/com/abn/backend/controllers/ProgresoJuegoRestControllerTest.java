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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        // Preparem el DTO de pujada (Unity -> Spring)
        progresoUpdateDTO = new ProgresoUpdateDTO();
        progresoUpdateDTO.setEstrellasGanadas(3);
        progresoUpdateDTO.setDesbloqueado(true);
        progresoUpdateDTO.setTiempoSegundos(45.5);
        progresoUpdateDTO.setIntentosFallidos(2);

        // Preparem la resposta simulada (Spring -> Unity)
        progresoResponseDTO = new ProgresoResponseDTO();
        progresoResponseDTO.setId(1L);
        progresoResponseDTO.setIdJuego(1301);
        progresoResponseDTO.setEstrellasGanadas(3);
    }

    @Test
    @DisplayName("PUT /api/progresos/1 - Èxit en la sincronització")
    void actualizarProgreso_Exito() throws Exception {
        // GIVEN
        when(progresoService.actualizarProgreso(eq(1L), any(ProgresoUpdateDTO.class)))
                .thenReturn(progresoResponseDTO);

        // WHEN & THEN
        mockMvc.perform(put("/api/progresos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(progresoUpdateDTO)))
                .andExpect(status().isOk()) // Verifica codi 200
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estrellasGanadas").value(3));

        verify(progresoService).actualizarProgreso(eq(1L), any(ProgresoUpdateDTO.class));
    }

    @Test
    @DisplayName("PUT /api/progresos/1 - Error per dades invàlides (Validació)")
    void actualizarProgreso_BadRequest() throws Exception {
        // GIVEN: Suposem que les estrelles no poden ser negatives (segons el teu @Min al DTO)
        progresoUpdateDTO.setEstrellasGanadas(-1);

        // WHEN & THEN
        mockMvc.perform(put("/api/progresos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(progresoUpdateDTO)))
                .andExpect(status().isBadRequest()); // Verifica 400 Bad Request

        // El servei no s'hauria de cridar mai si la validació de Bean Validation falla
        verify(progresoService, never()).actualizarProgreso(any(), any());
    }
}