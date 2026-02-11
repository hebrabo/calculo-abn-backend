package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.InfantCreateDTO;
import com.abn.backend.dto.request.update.InfantUpdateDTO;
import com.abn.backend.dto.response.InfantResponseDTO;
import com.abn.backend.service.InfantPerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/infantes")
@RequiredArgsConstructor
public class InfantPerfilRestController {

    private final InfantPerfilService infantService;

    @GetMapping
    public ResponseEntity<List<InfantResponseDTO>> obtenerTodosLosInfantes() {
        List<InfantResponseDTO> infantes = infantService.obtenerTodosLosInfantes();
        if (infantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(infantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfantResponseDTO> obtenerInfantePorId(@PathVariable Long id) {
        InfantResponseDTO dto = infantService.obtenerInfantePorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<InfantResponseDTO> crearInfante(@Valid @RequestBody InfantCreateDTO dto) {
        InfantResponseDTO creado = infantService.crearInfante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InfantResponseDTO> actualizarInfante(@PathVariable Long id, @Valid @RequestBody InfantUpdateDTO dto) {
        InfantResponseDTO actualizado = infantService.actualizarInfante(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInfante(@PathVariable Long id) {
        infantService.eliminarInfante(id);
        return ResponseEntity.noContent().build();
    }
}