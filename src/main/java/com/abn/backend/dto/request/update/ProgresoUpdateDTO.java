package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProgresoUpdateDTO {
    @Min(0) @Max(3)
    private int estrellasGanadas;

    private boolean desbloqueado;
}
