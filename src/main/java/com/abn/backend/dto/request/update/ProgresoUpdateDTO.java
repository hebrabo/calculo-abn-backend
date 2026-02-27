package com.abn.backend.dto.request.update;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO DE TELEMETRIA I RENDIMENT
 * Aquest objecte és el "pont" per on viatgen els resultats pedagògics
 * des dels 100 jocs d'Unity fins a la base de dades PostgreSQL a AWS.
 */
@Data // Lombok: Genera automàticament Getters i Setters.
// Essencial per a que Jackson (el serialitzador de Spring) pugui omplir els camps des del JSON d'Unity.
public class ProgresoUpdateDTO {

    /**
     * BEAN VALIDATION: Regla de negoci ABN.
     * @Min(0) i @Max(3): Garanteixen la integritat del sistema de recompenses.
     * Cap joc pot retornar un valor d'estrelles que estigui fora del rang pedagògic (0-3).
     */
    @Min(0)
    @Max(3)
    private int estrellasGanadas;

    /**
     * @NotNull: Comprovació de seguretat.
     * Ens assegurem que Unity ens enviï sempre l'estat del joc.
     * Un valor 'true' indica que el nen ha superat la fita i pot desbloquejar el següent contingut.
     */
    @NotNull
    private boolean desbloqueado;

    /**
     * MÉTRICA DE TEMPS:
     * Utilitzem 'double' per tenir precisió decimal en el cronòmetre de Unity.
     * @Min(0): Protecció contra dades corruptes (el temps mai pot ser negatiu).
     */
    @Min(0)
    private double tiempoSegundos;

    /**
     * MÉTRICA D'ERRORS:
     * Clau per a l'analítica del tutor. Si un nen té molts intents fallits,
     * el sistema de Spring Boot ho detectarà per mostrar una alerta al mestre.
     */
    @Min(0)
    private int intentosFallidos;
}