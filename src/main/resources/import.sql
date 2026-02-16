-- 1. Primer la Configuració (perquè el tutor en depèn)
INSERT INTO configuraciones_tutor (id, musica_activada, volumen_efectos, idioma)
VALUES (1, true, 75, 'ca');

-- 2. El Tutor (apuntant a la configuració creada)
INSERT INTO tutores_perfil (id, email, configuracion_id)
VALUES (1, 'profe_acces_dades@test.com', 1);

-- 3. L'Infant (edat 4 dins del rang 3-5)
INSERT INTO infantes_perfil (id, nombre, edad, avatar, tutor_id)
VALUES (1, 'Joan ABN', 4, 'avatar_nen_01', 1);

-- 4. Progrés de joc
INSERT INTO progresos_juego (id, id_juego, nombre_juego, desbloqueado, estrellas_ganadas, intentos_fallidos, tiempo_segundos, infante_id)
VALUES (1, 101, 'Comptar fins a 5', true, 3, 1, 35.5, 1);