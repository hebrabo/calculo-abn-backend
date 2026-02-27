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

/**
 * CONTROLADOR DE TELEMETRIA I PROGRESSOS
 * Aquest component gestiona l'evolució pedagògica de l'infant.
 * És el punt on les dades es converteixen en informació sobre l'aprenentatge.
 */
@CrossOrigin // Fonamental per permetre que Unity (client) enviï dades a AWS (servidor)
@RestController
@RequestMapping("/api/progresos")
@RequiredArgsConstructor // Injecció de dependències per constructor (Best Practice per a testabilitat)
@Tag(name = "Progressos i Fites", description = "Endpoints per al seguiment del rendiment i desbloqueig de jocs")
public class ProgresoJuegoRestController {

    private final ProgresoJuegoService progresoService;

    /**
     * READ (GET) - FILTRATGE DE DADES
     * Mostra com l'API permet fer consultes relacionals: "Dona'm els progressos d'un infant concret".
     * @param infantId ID de l'infant (clau forana a la base de dades).
     */
    @Operation(summary = "Obté els progressos d'un infant", description = "Retorna la llista de tots els jocs intentats o superats per un infant específic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista de progressos trobada"),
            @ApiResponse(responseCode = "204", description = "L'infant encara no té registres de progrés"),
            @ApiResponse(responseCode = "404", description = "Infant no trobat")
    })
    @GetMapping("/infante/{infantId}")
    public ResponseEntity<List<ProgresoResponseDTO>> obtenerProgresosPorInfante(@PathVariable Long infantId) {
        List<ProgresoResponseDTO> progresos = progresoService.obtenerProgresosPorInfante(infantId);

        // Ús correcte dels codis HTTP: Si la consulta és vàlida però no hi ha dades,
        // retornem 204 (No Content) per estalviar amplada de banda i processament al client.
        if (progresos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(progresos);
    }

    /**
     * UPDATE (PUT) - PERSISTÈNCIA D'ESTATS
     * Cada vegada que un nen acaba un joc a Unity, s'envia aquesta petició per actualitzar
     * les estrelles, el temps i els errors al núvol (AWS RDS).
     */
    @Operation(summary = "Actualitza el progrés d'un joc", description = "Registra nous resultats (estrelles, intents) per a un joc i infant concrets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progrés actualitzat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades de progrés invàlides"),
            @ApiResponse(responseCode = "404", description = "Registre de progrés no trobat")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProgresoResponseDTO> actualizarProgreso(
            @PathVariable Long id,
            @Valid @RequestBody ProgresoUpdateDTO dto) {

        // El DTO encapsula la informació que ens arriba de Unity.
        // El mètode és PUT perquè estem modificant un recurs ja identificat per la seva ID.
        ProgresoResponseDTO actualizado = progresoService.actualizarProgreso(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * LÒGICA DE NEGOCI (GET) - CONTROL D'ACCÉS
     * Aquest és un endpoint "intel·ligent". No només retorna dades de la DB,
     * sinó que executa la lògica de desbloqueig del mètode ABN.
     */
    @Operation(summary = "Comprova accés a un joc", description = "Determina si un infant pot accedir a un joc segons la lògica de desbloqueig del mètode ABN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realitzada (retorna true o false)"),
            @ApiResponse(responseCode = "404", description = "Infant o joc no identificat")
    })
    @GetMapping("/puede-jugar/{infantId}/{juegoId}")
    public ResponseEntity<Boolean> comprobarAcceso(
            @PathVariable Long infantId,
            @PathVariable int juegoId) {

        // Aquesta crida demostra la separació de capes: el controlador rep la pregunta,
        // el servei calcula la resposta basant-se en l'historial del nen.
        boolean puedeJugar = progresoService.puedeDesbloquearJuego(infantId, juegoId);
        return ResponseEntity.ok(puedeJugar);
    }
}