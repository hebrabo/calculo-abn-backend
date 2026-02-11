package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.service.TutorPerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tutores")
@RequiredArgsConstructor
public class TutorPerfilRestController {

    private final TutorPerfilService tutorService;

    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> obtenerTodosLosTutores() {
        List<TutorResponseDTO> tutores = tutorService.obtenerTodosLosTutores();
        if (tutores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tutores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> obtenerTutorPorId(@PathVariable Long id) {
        TutorResponseDTO dto = tutorService.obtenerTutorPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TutorResponseDTO> crearTutor(@Valid @RequestBody TutorCreateDTO dto) {
        TutorResponseDTO creado = tutorService.crearTutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> actualizarTutor(@PathVariable Long id, @Valid @RequestBody TutorUpdateDTO dto) {
        TutorResponseDTO actualizado = tutorService.actualizarTutor(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTutor(@PathVariable Long id) {
        tutorService.eliminarTutor(id);
        return ResponseEntity.noContent().build();
    }
}