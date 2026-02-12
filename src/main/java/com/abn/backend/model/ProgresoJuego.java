package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "progresos_juego")
public class ProgresoJuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long idJuego;
    private String nombreJuego;
    private boolean desbloqueado;
    private int estrellasGanadas;

    // NOVES COLUMNES PER A L'ANALÍTICA
    private double tiempoSegundos;   // Quant ha trigat el nen
    private int intentosFallidos;    // Quants errors ha fet

    @ManyToOne
    @JoinColumn(name = "infante_id", nullable = false)
    @JsonIgnore
    private InfantPerfil infante;
}