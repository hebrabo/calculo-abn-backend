package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.service.InfantPerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/infantes")
@RequiredArgsConstructor
@Tag(name = "Infants", description = "Endpoints per a la gestió dels perfils d'infants (3-5 anys)")
public class InfantPerfilRestController {

    private final InfantPerfilService infantService;

    @Operation(summary = "Llista tots els infants", description = "Retorna una llista completa de tots els perfils d'infants registrats.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista obtinguda correctament"),
            @ApiResponse(responseCode = "204", description = "No hi ha infants registrats")
    })
    @GetMapping
    public ResponseEntity<List<InfantResponseDTO>> obtenerTodosLosInfantes() {
        List<InfantResponseDTO> infantes = infantService.obtenerTodosLosInfantes();
        if (infantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(infantes);
    }

    @Operation(summary = "Obté un infant per ID", description = "Retorna la informació detallada d'un infant específic mitjançant el seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Infant trobat correctament"),
            @ApiResponse(responseCode = "404", description = "Infant no trobat")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InfantResponseDTO> obtenerInfantePorId(@PathVariable Long id) {
        InfantResponseDTO dto = infantService.obtenerInfantePorId(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Registra un nou infant", description = "Crea un nou perfil d'infant associat a un tutor i retorna l'objecte creat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Infant creat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades d'entrada invàlides")
    })
    @PostMapping
    public ResponseEntity<InfantResponseDTO> crearInfante(@Valid @RequestBody InfantCreateDTO dto) {
        InfantResponseDTO creado = infantService.crearInfante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualitza les dades d'un infant", description = "Modifica la informació d'un perfil d'infant existent (nom, avatar, edat).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Infant actualitzat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades de l'actualització invàlides"),
            @ApiResponse(responseCode = "404", description = "Infant no trobat")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InfantResponseDTO> actualizarInfante(@PathVariable Long id, @Valid @RequestBody InfantUpdateDTO dto) {
        InfantResponseDTO actualizado = infantService.actualizarInfante(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Elimina un perfil d'infant", description = "Elimina de forma permanent el registre d'un infant de la base de dades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Infant eliminat correctament"),
            @ApiResponse(responseCode = "404", description = "Infant no trobat")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInfante(@PathVariable Long id) {
        infantService.eliminarInfante(id);
        return ResponseEntity.noContent().build();
    }
}