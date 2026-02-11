package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "infantes_perfil")
public class InfantPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String avatar;
    private int edad;

    // Relació: Molts nens pertanyen a un sol Tutor
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private TutorPerfil tutor;

    @OneToMany(mappedBy = "infante", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ProgresoJuego> progresos;
}
