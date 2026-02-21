package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.service.ConfiguracionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
@Tag(name = "Configuració", description = "Endpoints per a la gestió de la configuració personalitzada dels tutors")
public class ConfiguracionRestController {

    private final ConfiguracionService configuracionService;

    @Operation(summary = "Obté la configuració d'un tutor", description = "Retorna els paràmetres de configuració associats a un ID de tutor específic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuració trobada correctament"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<ConfiguracionResponseDTO> obtenerConfiguracion(@PathVariable Long tutorId) {
        ConfiguracionResponseDTO dto = configuracionService.obtenerConfiguracionPorTutor(tutorId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Actualitza la configuració d'un tutor", description = "Modifica els paràmetres de configuració (sons, dificultat, etc.) per a un tutor existent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuració actualitzada correctament"),
            @ApiResponse(responseCode = "400", description = "Dades de l'actualització incorrectes o invàlides"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @PutMapping("/tutor/{tutorId}")
    public ResponseEntity<ConfiguracionResponseDTO> actualizarConfiguracion(
            @PathVariable Long tutorId,
            @Valid @RequestBody ConfiguracionUpdateDTO dto) {
        ConfiguracionResponseDTO actualizada = configuracionService.actualizarConfiguracion(tutorId, dto);
        return ResponseEntity.ok(actualizada);
    }
}