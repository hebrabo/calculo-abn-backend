-- 1. Insertamos la Configuración (Tabla: configuraciones_tutor)
INSERT INTO configuraciones_tutor (id, musica_activada, volumen_efectos, idioma)
VALUES (1, true, 75, 'es');

-- 2. Insertamos el Tutor (Tabla: tutores_perfil)
-- Conectamos con la configuración ID 1
INSERT INTO tutores_perfil (id, email, configuracion_id)
VALUES (1, 'profe_acces_datos@test.com', 1);

-- 3. Insertamos el Infante (Tabla: infantes_perfil)
-- Edad de 4 años, dentro del rango 3-5 para la app ABN
INSERT INTO infantes_perfil (id, nombre, edad, avatar, tutor_id)
VALUES (1, 'Juan ABN', 4, 'avatar_nino_01', 1);

-- 4. Insertamos el Progreso (Tabla: progresos_juego)
-- Reflejamos la analítica: intentos fallidos y tiempo
INSERT INTO progresos_juego (id, id_juego, nombre_juego, desbloqueado, estrellas_ganadas, intentos_fallidos, tiempo_segundos, infante_id)
VALUES (1, 101, 'Contar hasta 5', true, 3, 1, 35.5, 1);