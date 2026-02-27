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

/**
 * CAPA DE PRESENTACIÓ (REST CONTROLLER)
 * Aquest controlador actua com a punt d'entrada per a les peticions HTTP relacionades
 * amb la configuració de l'app (volum, sons, idioma).
 */
@CrossOrigin // Permet peticions des d'altres dominis (com Unity en WebGL o mòbil)
@RestController // Indica que la resposta serà automàticament convertida a JSON
@RequestMapping("/api/configuracion") // Defineix l'arrel de la URL per a aquest recurs
@RequiredArgsConstructor // Genera el constructor per a la injecció de dependències dels camps 'final'
@Tag(name = "Configuració", description = "Endpoints per a la gestió de la configuració personalitzada dels tutors")
public class ConfiguracionRestController {

    // Ús d'injecció de dependències per constructor (recomanat per sobre d'@Autowired per facilitar els testos unitaris)
    private final ConfiguracionService configuracionService;

    /**
     * LLEGIR (GET): Obtenció de recursos.
     * Utilitzem @PathVariable per identificar el recurs (tutorId) directament a la URL.
     */
    @Operation(summary = "Obté la configuració d'un tutor", description = "Retorna els paràmetres de configuració associats a un ID de tutor específic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuració trobada correctament"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<ConfiguracionResponseDTO> obtenerConfiguracion(@PathVariable Long tutorId) {
        // Separem la lògica de negoci al Service per mantenir el Controller "lleuger"
        ConfiguracionResponseDTO dto = configuracionService.obtenerConfiguracionPorTutor(tutorId);
        // Retornem un ResponseEntity per tenir control total sobre el codi d'estat HTTP (200 OK)
        return ResponseEntity.ok(dto);
    }

    /**
     * ACTUALITZAR (PUT): Modificació de recursos existents.
     * @Valid: Activa les validacions de Jakarta Bean Validation definides al DTO (com @NotNull).
     * @RequestBody: Deserialitza automàticament el JSON rebut de Unity en un objecte Java.
     */
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

        // Cridem al servei per realitzar la persistència a la base de dades (AWS RDS)
        ConfiguracionResponseDTO actualizada = configuracionService.actualizarConfiguracion(tutorId, dto);
        return ResponseEntity.ok(actualizada);
    }
}