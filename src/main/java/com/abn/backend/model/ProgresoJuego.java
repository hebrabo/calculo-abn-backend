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
    private int estrellasGanadas; // Per saber si ho ha fet molt bé (0 a 3)

    // Cada registre de progrés pertany a un nen concret
    @ManyToOne
    @JoinColumn(name = "infante_id", nullable = false)
    @JsonIgnore
    private InfantPerfil infante;
}