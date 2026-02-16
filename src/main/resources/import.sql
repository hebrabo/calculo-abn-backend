-- 1. Crea un Tutor de prova
INSERT INTO tutores (id, email) VALUES (1, 'profe_acces_dades@test.com');

-- 2. Crea la seva Configuració (Relació 1:1)
-- Nota: Els noms de les columnes coincideixen exactament amb les  entitats Java
INSERT INTO configuraciones (id, musica_activada, volumen_efectos, idioma, tutor_id) VALUES (1, true, 75, 'ca', 1);

-- 3. Crea un Infant de prova (Relació 1:N amb Tutor)
-- Edat de 4 anys (dins del rang 3-5)
INSERT INTO infantes (id, nombre, edad, avatar, tutor_id) VALUES (1, 'Joan ABN', 4, 'avatar_nen_01', 1);

-- 4. Crea un progrés inicial en un dels diferents jocs (Relació 1:N amb Infant)
INSERT INTO progresos_juego (id, juego_id, estrellas, completado, intentos_fallidos, tiempo_segundos, infante_id) VALUES (1, 'joc_comptar_001', 3, true, 1, 35.5, 1);