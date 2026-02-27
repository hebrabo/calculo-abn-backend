package com.abn.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abn.backend.dto.request.update.ConfiguracionUpdateDTO;
import com.abn.backend.dto.response.ConfiguracionResponseDTO;
import com.abn.backend.mapper.ConfiguracionMapper;
import com.abn.backend.model.TutorPerfil;
import com.abn.backend.repository.TutorPerfilRepository;
import com.abn.backend.service.ConfiguracionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * IMPLEMENTACIÓ DEL SERVEI DE CONFIGURACIÓ
 * Aquí és on resideix la lògica de control. Aquesta classe orquestra
 * el Repositori i el Mapper per complir el contracte del servei.
 */
@Service // Marca la classe com a component de servei en el context de Spring.
@RequiredArgsConstructor // Genera el constructor per a la Injecció de Dependències (final fields).
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final TutorPerfilRepository tutorRepository;
    private final ConfiguracionMapper configuracionMapper;

    /**
     * ACTUALITZACIÓ DE CONFIGURACIÓ
     * @Transactional: Assegura que tota l'operació (cerca, mapeig i guardat) es realitzi
     * dins d'una transacció de base de dades. Si alguna cosa falla, es fa un rollback.
     */
    @Override
    @Transactional
    public ConfiguracionResponseDTO actualizarConfiguracion(Long tutorId, ConfiguracionUpdateDTO dto) {
        // 1. Recuperem el Tutor (que conté la configuració) o llancem excepció si no existeix.
        TutorPerfil tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado"));

        // 2. El Mapper actualitza l'entitat existent amb les dades del DTO.
        configuracionMapper.updateConfigFromDto(dto, tutor.getConfiguracion());

        // 3. Desem els canvis i retornem el resultat convertit a DTO de resposta.
        return configuracionMapper.toDto(tutorRepository.save(tutor).getConfiguracion());
    }

    /**
     * CONSULTA DE CONFIGURACIÓ
     * Operació de només lectura. No requereix @Transactional de tipus escriptura.
     */
    @Override
    public ConfiguracionResponseDTO obtenerConfiguracionPorTutor(Long tutorId) {
        TutorPerfil tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor no encontrado"));

        return configuracionMapper.toDto(tutor.getConfiguracion());
    }
}