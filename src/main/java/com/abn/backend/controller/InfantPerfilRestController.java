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

/**
 * CONTROLADOR REST - GESTIÓ D'INFANTS
 * Implementa un CRUD complet (Create, Read, Update, Delete) seguint els estàndards RESTful.
 */
@CrossOrigin // Habilita el mecanisme CORS per permetre connexions des del client Unity
@RestController // Combinació de @Controller i @ResponseBody: retorna dades directament al cos (JSON)
@RequestMapping("/api/infantes") // URI base en plural, seguint les convencions de disseny d'APIs
@RequiredArgsConstructor // Lombok: Injecta el servei mitjançant el constructor (Immutabilitat)
@Tag(name = "Infants", description = "Endpoints per a la gestió dels perfils d'infants (3-5 anys)")
public class InfantPerfilRestController {

    private final InfantPerfilService infantService;

    /**
     * READ ALL (GET): Obté la col·lecció completa d'infants.
     * Retorna 204 (No Content) si la llista és buida, una pràctica més neta que retornar un 200 amb llista buida.
     */
    @Operation(summary = "Llista tots els infants", description = "Retorna una llista completa de tots els perfils d'infants registrats.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista obtinguda correctament"),
            @ApiResponse(responseCode = "204", description = "No hi ha infants registrats")
    })
    @GetMapping
    public ResponseEntity<List<InfantResponseDTO>> obtenerTodosLosInfantes() {
        List<InfantResponseDTO> infantes = infantService.obtenerTodosLosInfantes();
        if (infantes.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.ok(infantes); // HTTP 200
    }

    /**
     * READ ONE (GET): Obté un recurs específic per ID.
     * @PathVariable: Extreu el paràmetre directament del segment de la URL.
     */
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

    /**
     * CREATE (POST): Crea un nou recurs.
     * @Valid: Força la validació de les regles de negoci definides al DTO abans d'executar el mètode.
     * Retorna HttpStatus.CREATED (201), el codi estàndard per a creacions exitoses.
     */
    @Operation(summary = "Registra un nou infant", description = "Crea un nou perfil d'infant associat a un tutor i retorna l'objecte creat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Infant creat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades d'entrada invàlides")
    })
    @PostMapping
    public ResponseEntity<InfantResponseDTO> crearInfante(@Valid @RequestBody InfantCreateDTO dto) {
        InfantResponseDTO creado = infantService.crearInfante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado); // HTTP 201
    }

    /**
     * UPDATE (PUT): Actualitza un recurs existent de forma integral.
     */
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

    /**
     * DELETE (DELETE): Elimina un recurs.
     * Retorna 204 (No Content) per indicar que l'acció s'ha realitzat però ja no hi ha cap entitat per retornar.
     */
    @Operation(summary = "Elimina un perfil d'infant", description = "Elimina de forma permanent el registre d'un infant de la base de dades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Infant eliminat correctament"),
            @ApiResponse(responseCode = "404", description = "Infant no trobat")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInfante(@PathVariable Long id) {
        infantService.eliminarInfante(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}