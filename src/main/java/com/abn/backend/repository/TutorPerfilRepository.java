package com.abn.backend.repository;

import com.abn.backend.model.TutorPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorPerfilRepository extends JpaRepository<TutorPerfil, Long> {

}
