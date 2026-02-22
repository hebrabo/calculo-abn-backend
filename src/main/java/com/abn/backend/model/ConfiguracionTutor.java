package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "configuraciones_tutor")
public class ConfiguracionTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean musicaActivada;
    private int volumenEfectos;
    private String idioma;

    // --- EL CANVI CLAU PER A LA SINCRONITZACIÓ (ESTIL PROFE) ---
    @OneToOne
    @JoinColumn(name = "id_tutor") // Aquí és on es crea físicament la columna a la BD
    @ToString.Exclude // Evitem bucles infinits al fer log del tutor
    private TutorPerfil tutor;
}