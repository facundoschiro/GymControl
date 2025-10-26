CREATE DATABASE IF NOT EXISTS gymcontrol CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE gymcontrol;

CREATE TABLE socio (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  dni VARCHAR(15) NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_alta DATE NOT NULL,
  apto_medico_hasta DATE NULL,
  UNIQUE KEY uq_socio_dni (dni)
);

CREATE TABLE instructor (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  especialidad VARCHAR(120) NULL
);

CREATE TABLE ejercicio (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  grupo_muscular VARCHAR(80) NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE cuota (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  periodo_desde DATE NOT NULL,
  periodo_hasta DATE NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  estado ENUM('PENDIENTE','VIGENTE','VENCIDA') NOT NULL DEFAULT 'PENDIENTE',
  CONSTRAINT fk_cuota_socio FOREIGN KEY (socio_id) REFERENCES socio(id),
  UNIQUE KEY uq_cuota_periodo (socio_id, periodo_desde, periodo_hasta)
);

CREATE TABLE pago (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  cuota_id BIGINT NOT NULL,
  fecha DATE NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  medio ENUM('EFECTIVO','DEBITO','TRANSFERENCIA') NOT NULL,
  anulado BOOLEAN NOT NULL DEFAULT FALSE,
  motivo_anulacion VARCHAR(200) NULL,
  CONSTRAINT fk_pago_cuota FOREIGN KEY (cuota_id) REFERENCES cuota(id),
  UNIQUE KEY uq_pago_unico_por_cuota (cuota_id)  -- un pago por cuota (ajustar si permiten múltiples)
);

CREATE TABLE acceso (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  fecha_hora DATETIME NOT NULL,
  resultado ENUM('PERMITIDO','DENEGADO') NOT NULL,
  motivo VARCHAR(120) NULL,
  CONSTRAINT fk_acceso_socio FOREIGN KEY (socio_id) REFERENCES socio(id),
  KEY idx_acceso_fh (fecha_hora)
);

CREATE TABLE asistencia (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  fecha DATE NOT NULL,
  CONSTRAINT fk_asistencia_socio FOREIGN KEY (socio_id) REFERENCES socio(id),
  UNIQUE KEY uq_asistencia_un_dia (socio_id, fecha)
);

CREATE TABLE rutina (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  instructor_id BIGINT NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  activa BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_rutina_socio FOREIGN KEY (socio_id) REFERENCES socio(id),
  CONSTRAINT fk_rutina_instructor FOREIGN KEY (instructor_id) REFERENCES instructor(id)
);

CREATE TABLE rutina_detalle (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  rutina_id BIGINT NOT NULL,
  ejercicio_id BIGINT NOT NULL,
  dia_semana TINYINT NOT NULL,
  series_sug INT NULL,
  reps_sug INT NULL,
  peso_sug DECIMAL(8,2) NULL,
  CONSTRAINT fk_rdet_rutina FOREIGN KEY (rutina_id) REFERENCES rutina(id),
  CONSTRAINT fk_rdet_ej FOREIGN KEY (ejercicio_id) REFERENCES ejercicio(id)
);

CREATE TABLE entrenamiento (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  instructor_id BIGINT NOT NULL,
  fecha DATE NOT NULL,
  estado ENUM('ABIERTA','CERRADA') NOT NULL DEFAULT 'ABIERTA',
  observaciones VARCHAR(250) NULL,
  CONSTRAINT fk_ent_socio FOREIGN KEY (socio_id) REFERENCES socio(id),
  CONSTRAINT fk_ent_instr FOREIGN KEY (instructor_id) REFERENCES instructor(id),
  UNIQUE KEY uq_entrenamiento_dia (socio_id, fecha)
);

CREATE TABLE entrenamiento_detalle (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  entreno_id BIGINT NOT NULL,
  ejercicio_id BIGINT NOT NULL,
  nro_serie INT NOT NULL,
  reps INT NOT NULL,
  peso DECIMAL(8,2) NOT NULL,
  CONSTRAINT fk_ed_ent FOREIGN KEY (entreno_id) REFERENCES entrenamiento(id) ON DELETE CASCADE,
  CONSTRAINT fk_ed_ej FOREIGN KEY (ejercicio_id) REFERENCES ejercicio(id),
  KEY idx_ed_ent (entreno_id),
  UNIQUE KEY uq_serie_unica (entreno_id, ejercicio_id, nro_serie)
);
