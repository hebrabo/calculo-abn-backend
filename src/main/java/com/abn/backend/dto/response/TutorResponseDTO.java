package com.abn.backend.dto.response;

import lombok.Data;
import java.util.List;

/**
 * DTO DE RESPOSTA - PERFIL DEL TUTOR
 * Aquest objecte representa la visió pública de l'usuari principal.
 * És el recurs arrel del qual pengen les configuracions i els perfils dels nens.
 */
@Data // Lombok: Genera Getters, Setters i mètodes d'utilitat.
// Permet que Jackson transformi l'entitat de la DB en un JSON net per a Unity.
public class TutorResponseDTO {

    /**
     * ID de referència a AWS RDS.
     * Unity la guardarà a 'TutorIDRemoto' (PlayerPrefs) per a totes les operacions CRUD.
     */
    private Long id;

    private String email;

    /**
     * COMPOSICIÓ (Objecte Niuat):
     * Aquí enviem la 'ConfiguracionResponseDTO' completa.
     * Com que cada tutor té només UNA configuració (1:1), és eficient enviar-ho
     * tot en el mateix paquet de resposta.
     */
    private ConfiguracionResponseDTO configuracion;

    /**
     * RELACIÓ ONE-TO-MANY (Optimitzada):
     * En lloc d'enviar tots els perfils de nens detallats aquí dins (que podria fer
     * que el JSON pesés massa), enviem només una llista d'IDs.
     * Això demostra una estratègia de "Lazy Loading" a nivell d'API: Unity sap
     * quants nens hi ha, i si necessita els detalls, farà la crida a l'endpoint /infantes.
     */
    private List<Long> infantes;
}