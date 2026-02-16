package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO per rebre l'actualització de dades des d'Unity.
 * Coincideix amb els camps enviats per l'APIManager (estrellasGanadas, tiempoSegundos, etc.)
 */
@Data
public class ProgresoUpdateDTO {

    @Min(0)
    @Max(3)
    private int estrellasGanadas;

    @NotNull
    private boolean desbloqueado;

    @Min(0)
    private double tiempoSegundos;

    @Min(0)
    private int intentosFallidos;
}