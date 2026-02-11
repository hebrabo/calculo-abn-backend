package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ProgresoUpdateDTO;
import com.abn.backend.dto.response.ProgresoResponseDTO;
import com.abn.backend.service.ProgresoJuegoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progresos")
@RequiredArgsConstructor
public class ProgresoJuegoRestController {

    private final ProgresoJuegoService progresoService;

    @GetMapping("/infante/{infantId}")
    public ResponseEntity<List<ProgresoResponseDTO>> obtenerProgresosPorInfante(@PathVariable Long infantId) {
        List<ProgresoResponseDTO> progresos = progresoService.obtenerProgresosPorInfante(infantId);
        if (progresos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(progresos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgresoResponseDTO> actualizarProgreso(@PathVariable Long id, @Valid @RequestBody ProgresoUpdateDTO dto) {
        ProgresoResponseDTO actualizado = progresoService.actualizarProgreso(id, dto);
        return ResponseEntity.ok(actualizado);
    }
}