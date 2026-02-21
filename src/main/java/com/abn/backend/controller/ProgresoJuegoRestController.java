package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.service.ProgresoJuegoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/progresos")
@RequiredArgsConstructor
@Tag(name = "Progressos i Fites", description = "Endpoints per al seguiment del rendiment i desbloqueig de jocs")
public class ProgresoJuegoRestController {

    private final ProgresoJuegoService progresoService;

    @Operation(summary = "Obté els progressos d'un infant", description = "Retorna la llista de tots els jocs intentats o superats per un infant específic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista de progressos trobada"),
            @ApiResponse(responseCode = "204", description = "L'infant encara no té registres de progrés"),
            @ApiResponse(responseCode = "404", description = "Infant no trobat")
    })
    @GetMapping("/infante/{infantId}")
    public ResponseEntity<List<ProgresoResponseDTO>> obtenerProgresosPorInfante(@PathVariable Long infantId) {
        List<ProgresoResponseDTO> progresos = progresoService.obtenerProgresosPorInfante(infantId);
        if (progresos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(progresos);
    }

    @Operation(summary = "Actualitza el progrés d'un joc", description = "Registra nous resultats (estrelles, intents) per a un joc i infant concrets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progrés actualitzat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades de progrés invàlides"),
            @ApiResponse(responseCode = "404", description = "Registre de progrés no trobat")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProgresoResponseDTO> actualizarProgreso(@PathVariable Long id, @Valid @RequestBody ProgresoUpdateDTO dto) {
        ProgresoResponseDTO actualizado = progresoService.actualizarProgreso(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Comprova accés a un joc", description = "Determina si un infant pot accedir a un joc segons la lògica de desbloqueig del mètode ABN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realitzada (retorna true o false)"),
            @ApiResponse(responseCode = "404", description = "Infant o joc no identificat")
    })
    @GetMapping("/puede-jugar/{infantId}/{juegoId}")
    public ResponseEntity<Boolean> comprobarAcceso(@PathVariable Long infantId, @PathVariable int juegoId) {
        boolean puedeJugar = progresoService.puedeDesbloquearJuego(infantId, juegoId);
        return ResponseEntity.ok(puedeJugar);
    }
}