package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.service.TutorPerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR DE GESTIÓ DE TUTORS (L'Entitat Arrel)
 * Aquest controlador gestiona els usuaris principals de l'aplicació.
 * Segueix el patró de disseny RESTful per a la gestió de recursos.
 */
@CrossOrigin // Permet que el client Unity (potencialment en un domini diferent) consumeixi l'API
@RestController // Especifica que cada mètode retornarà dades (JSON) en lloc de vistes (HTML)
@RequestMapping("/api/tutores") // Base URI per al recurs Tutor
@RequiredArgsConstructor // Lombok: Implementa la injecció de dependències via constructor per als camps final
@Tag(name = "Tutors", description = "Endpoints per a la gestió de perfils de tutors i administradors")
public class TutorPerfilRestController {

    private final TutorPerfilService tutorService;

    /**
     * GET: Llista completa de tutors.
     * Útil per a panells d'administració. Retorna 204 si la base de dades és buida.
     */
    @Operation(summary = "Llista tots els tutors", description = "Retorna una llista de tots els tutors registrats en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista obtinguda correctament"),
            @ApiResponse(responseCode = "204", description = "No hi ha cap tutor registrat")
    })
    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> obtenerTodosLosTutores() {
        List<TutorResponseDTO> tutores = tutorService.obtenerTodosLosTutores();
        if (tutores.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retornem 204 No Content
        }
        return ResponseEntity.ok(tutores); // Retornem 200 OK
    }

    /**
     * GET /{id}: Obtenció d'un tutor per la seva Clau Primària.
     */
    @Operation(summary = "Obté un tutor per ID", description = "Retorna la informació d'un tutor específic mitjançant el seu identificador únic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor trobat correctament"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> obtenerTutorPorId(@PathVariable Long id) {
        TutorResponseDTO dto = tutorService.obtenerTutorPorId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * GET /{id}/infantes: RECURS ANIDAT
     * Aquest és un endpoint d'agregació molt potent. Segueix la jerarquia REST
     * per obtenir els fills (Infants) que pertanyen a un Tutor concret.
     * És la solució tècnica per evitar el 404 en la sincronització de Unity.
     */
    @Operation(summary = "Obté els infants d'un tutor", description = "Retorna la llista completa d'objectes infant associats a un tutor específic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista d'infants obtinguda correctament"),
            @ApiResponse(responseCode = "204", description = "El tutor no té infants assignats"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @GetMapping("/{id}/infantes")
    public ResponseEntity<List<InfantResponseDTO>> obtenerInfantesPorTutor(@PathVariable Long id) {
        List<InfantResponseDTO> infantes = tutorService.obtenerInfantesPorTutor(id);
        if (infantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(infantes);
    }

    /**
     * POST: Creació de nou recurs.
     * Retorna 201 Created. L'ús de TutorCreateDTO separa les dades d'entrada
     * de la lògica interna de l'entitat JPA.
     */
    @Operation(summary = "Registra un nou tutor", description = "Crea un nou compte de tutor en el sistema i retorna les seves dades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutor creat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades del nou tutor invàlides")
    })
    @PostMapping
    public ResponseEntity<TutorResponseDTO> crearTutor(@Valid @RequestBody TutorCreateDTO dto) {
        TutorResponseDTO creado = tutorService.crearTutor(dto);
        // HttpStatus.CREATED indica que s'ha generat un nou recurs amb èxit
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * PUT /{id}: Actualització integral del perfil del tutor.
     */
    @Operation(summary = "Actualitza les dades d'un tutor", description = "Modifica la informació de perfil d'un tutor existent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor actualitzat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades d'actualització invàlides"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> actualizarTutor(@PathVariable Long id, @Valid @RequestBody TutorUpdateDTO dto) {
        TutorResponseDTO actualizado = tutorService.actualizarTutor(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * DELETE /{id}: Eliminació física del recurs.
     */
    @Operation(summary = "Elimina un tutor", description = "Esborra de forma definitiva el compte d'un tutor i tota la seva configuració associada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tutor eliminat correctament"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTutor(@PathVariable Long id) {
        tutorService.eliminarTutor(id);
        return ResponseEntity.noContent().build(); // 204: Acció realitzada, sense contingut per retornar.
    }
}