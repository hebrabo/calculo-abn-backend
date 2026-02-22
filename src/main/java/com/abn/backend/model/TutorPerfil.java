package com.abn.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "tutores_perfil")
public class TutorPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tutor")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;


    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private ConfiguracionTutor configuracion;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfantPerfil> infantes;


    public void setConfiguracion(ConfiguracionTutor configuracion) {
        this.configuracion = configuracion;
        if (configuracion != null) {
            configuracion.setTutor(this);
        }
    }
}