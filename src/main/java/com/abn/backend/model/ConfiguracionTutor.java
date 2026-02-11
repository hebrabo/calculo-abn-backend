package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;

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
}