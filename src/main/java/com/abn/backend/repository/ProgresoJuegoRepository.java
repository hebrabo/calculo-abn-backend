package com.abn.backend.repository;

import com.abn.backend.model.ProgresoJuego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgresoJuegoRepository extends JpaRepository<ProgresoJuego, Long> {

}
