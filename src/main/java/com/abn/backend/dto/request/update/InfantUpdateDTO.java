package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InfantUpdateDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El avatar no puede estar vacío")
    private String avatar;

    @Min(3) @Max(6)
    private int edad;
}
