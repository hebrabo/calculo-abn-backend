-- 1. Crear taula de configuració
CREATE TABLE configuraciones_tutor (id bigint PRIMARY KEY, idioma varchar(255), musica_activada boolean NOT NULL, volumen_efectos integer NOT NULL);

-- 2. Crear taula de tutors
CREATE TABLE tutores_perfil (id bigint PRIMARY KEY, email varchar(255) UNIQUE NOT NULL, configuracion_id bigint UNIQUE REFERENCES configuraciones_tutor(id));

-- 3. Crear taula d'infants
CREATE TABLE infantes_perfil (id bigint PRIMARY KEY, nombre varchar(255), edad integer NOT NULL, avatar varchar(255), tutor_id bigint NOT NULL REFERENCES tutores_perfil(id));

-- 4. Crear taula de progressos
CREATE TABLE progresos_juego (id bigint PRIMARY KEY, id_juego bigint NOT NULL, nombre_juego varchar(255), desbloqueado boolean NOT NULL, estrellas_ganadas integer NOT NULL, intentos_fallidos integer NOT NULL, tiempo_segundos float8 NOT NULL, infante_id bigint NOT NULL REFERENCES infantes_perfil(id));