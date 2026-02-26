package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.dto.response.InfantResponseDTO; // <--- IMPORTANTE
import com.abn.backend.service.TutorPerfilService;
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
@RequestMapping("/api/tutores")
@RequiredArgsConstructor
@Tag(name = "Tutors", description = "Endpoints per a la gestió de perfils de tutors i administradors")
public class TutorPerfilRestController {

    private final TutorPerfilService tutorService;

    @Operation(summary = "Llista tots els tutors", description = "Retorna una llista de tots els tutors registrats en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Llista obtinguda correctament"),
            @ApiResponse(responseCode = "204", description = "No hi ha cap tutor registrat")
    })
    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> obtenerTodosLosTutores() {
        List<TutorResponseDTO> tutores = tutorService.obtenerTodosLosTutores();
        if (tutores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tutores);
    }

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

    // --- NOU ENDPOINT PER A UNITY (EVITA EL 404) ---
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

    @Operation(summary = "Registra un nou tutor", description = "Crea un nou compte de tutor en el sistema i retorna les seves dades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutor creat correctament"),
            @ApiResponse(responseCode = "400", description = "Dades del nou tutor invàlides")
    })
    @PostMapping
    public ResponseEntity<TutorResponseDTO> crearTutor(@Valid @RequestBody TutorCreateDTO dto) {
        TutorResponseDTO creado = tutorService.crearTutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

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

    @Operation(summary = "Elimina un tutor", description = "Esborra de forma definitiva el compte d'un tutor i tota la seva configuració associada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tutor eliminat correctament"),
            @ApiResponse(responseCode = "404", description = "Tutor no trobat")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTutor(@PathVariable Long id) {
        tutorService.eliminarTutor(id);
        return ResponseEntity.noContent().build();
    }
}