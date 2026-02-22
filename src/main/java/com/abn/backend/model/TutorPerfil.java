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
    @Column(name = "id_tutor") // Siguiendo el estilo id_usuario del profe
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // CAMBIO CLAVE: Usamos mappedBy para que el dueño de la relación sea ConfiguracionTutor
    // Esto evita conflictos de integridad al insertar porque la FK no está en esta tabla
    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private ConfiguracionTutor configuracion;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfantPerfil> infantes;

    // Método de sincronización bidireccional (Copiado del modelo del profe)
    public void setConfiguracion(ConfiguracionTutor configuracion) {
        this.configuracion = configuracion;
        if (configuracion != null) {
            configuracion.setTutor(this);
        }
    }
}