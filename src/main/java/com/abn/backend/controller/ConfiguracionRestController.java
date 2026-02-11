package com.abn.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.service.ConfiguracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
public class ConfiguracionRestController {

    private final ConfiguracionService configuracionService;

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<ConfiguracionResponseDTO> obtenerConfiguracion(@PathVariable Long tutorId) {
        ConfiguracionResponseDTO dto = configuracionService.obtenerConfiguracionPorTutor(tutorId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/tutor/{tutorId}")
    public ResponseEntity<ConfiguracionResponseDTO> actualizarConfiguracion(@PathVariable Long tutorId, @Valid @RequestBody ConfiguracionUpdateDTO dto) {
        ConfiguracionResponseDTO actualizada = configuracionService.actualizarConfiguracion(tutorId, dto);
        return ResponseEntity.ok(actualizada);
    }
}
