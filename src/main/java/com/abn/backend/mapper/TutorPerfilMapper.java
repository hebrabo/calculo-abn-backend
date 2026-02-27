package com.abn.backend.mapper;

import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.create.TutorCreateDTO;
import com.abn.backend.dto.request.update.TutorUpdateDTO;
import com.abn.backend.dto.response.TutorResponseDTO;
import com.abn.backend.model.ConfiguracionTutor;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.model.InfantPerfil;
import org.springframework.stereotype.Component;

/**
 * MAPPER DE PERFIL DE TUTOR
 * Aquesta classe gestiona la transformació de l'entitat principal del sistema.
 * Implementa el patró de "Mapeig de Composició" en delegar part de la feina
 * a altres mappers especialitzats.
 */
@Component // Registra la classe com a component de Spring (Bean)
@RequiredArgsConstructor // Lombok: Genera el constructor per a la injecció de dependències
public class TutorPerfilMapper {

    // Delegació de responsabilitat: Aquest mapper no sap com mapejar una configuració,
    // per això injecta el mapper especialitzat. Principi de Responsabilitat Única.
    private final ConfiguracionMapper configuracionMapper;

    /**
     * ENTITAT -> DTO (Sortida cap a Unity)
     * Converteix el model complex de la DB en una resposta simplificada.
     */
    public TutorResponseDTO toDto(TutorPerfil tutor) {
        if (tutor == null) return null;

        TutorResponseDTO dto = new TutorResponseDTO();
        dto.setId(tutor.getId());
        dto.setEmail(tutor.getEmail());

        // MAPEIG NIUAT: Cridem al mapper de configuració per transformar l'objecte fill.
        dto.setConfiguracion(configuracionMapper.toDto(tutor.getConfiguracion()));

        /**
         * ESTRATÈGIA DE FLATTENING (Aplanament):
         * En lloc d'enviar tots els objectes 'Infant' amb els seus 100 jocs,
         * només n'extraiem els IDs. Això optimitza el tràfic de xarxa i evita
         * problemes de recursivitat infinita en el JSON.
         */
        if (tutor.getInfantes() != null) {
            dto.setInfantes(tutor.getInfantes().stream()
                    .map(InfantPerfil::getId) // Transformem l'objecte en un simple Long
                    .toList());
        } else {
            dto.setInfantes(null);
        }

        return dto;
    }

    /**
     * DTO -> ENTITAT (Creació des de Unity)
     * Transforma les dades de registre en un model que JPA pot persistir a AWS RDS.
     */
    public TutorPerfil toEntity(TutorCreateDTO dto) {
        if (dto == null) return null;

        TutorPerfil tutor = new TutorPerfil();
        tutor.setEmail(dto.getEmail());

        /**
         * GESTIÓ DE RELACIONS 1:1:
         * Quan creem un tutor, també instanciem la seva configuració inicial.
         * Això a la base de dades es tradueix en una operació amb FK.
         */
        if (dto.getConfiguracion() != null) {
            ConfiguracionTutor config = configuracionMapper.toEntity(dto.getConfiguracion());
            tutor.setConfiguracion(config);
        }

        return tutor;
    }

    /**
     * ACTUALITZACIÓ DE L'ESTAT (Deep Update):
     * Permet modificar el perfil del tutor i la seva configuració niuada en un sol pas.
     */
    public void updateTutorFromDto(TutorUpdateDTO dto, TutorPerfil tutor) {
        if (dto == null || tutor == null) return;

        tutor.setEmail(dto.getEmail());

        // Actualització en cascada manual: Si el DTO porta nova configuració,
        // deleguem l'actualització al mapper fill sobre l'entitat ja existent.
        if (dto.getConfiguracion() != null && tutor.getConfiguracion() != null) {
            configuracionMapper.updateConfigFromDto(dto.getConfiguracion(), tutor.getConfiguracion());
        }
    }
}