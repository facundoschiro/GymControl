INSERT INTO socio (dni,nombre,estado,fecha_alta,apto_medico_hasta)
VALUES ('30111222','Ana Pérez','ACTIVO','2024-01-10','2026-01-10'),
       ('30222333','Luis Gómez','ACTIVO','2023-11-05','2026-06-01'),
       ('30111444','Sofía Díaz','INACTIVO','2022-03-02','2025-03-02');

INSERT INTO instructor (nombre,especialidad)
VALUES ('Martín Ruiz','Fuerza'),('Carla Núñez','Funcional');

INSERT INTO ejercicio (nombre,grupo_muscular) VALUES
('Banco plano','Pectoral'),('Sentadilla','Piernas'),('Press Militar','Hombros');

INSERT INTO cuota (socio_id,periodo_desde,periodo_hasta,monto,estado)
VALUES (1,'2025-10-01','2025-10-31',25000,'VIGENTE'),
       (2,'2025-09-01','2025-09-30',25000,'VENCIDA');

INSERT INTO pago (cuota_id,fecha,monto,medio) VALUES (1,'2025-10-01',25000,'EFECTIVO');

INSERT INTO acceso (socio_id,fecha_hora,resultado,motivo) VALUES
(1,'2025-10-10 18:05:00','PERMITIDO','OK'),
(1,'2025-10-10 19:01:00','PERMITIDO','OK'),
(2,'2025-10-10 18:10:00','DENEGADO','CUOTA_NO_VIGENTE');

INSERT INTO asistencia (socio_id,fecha) VALUES
(1,'2025-10-10');

INSERT INTO entrenamiento (socio_id,instructor_id,fecha,estado,observaciones)
VALUES (1,1,'2025-10-10','ABIERTA','Pecho');

INSERT INTO entrenamiento_detalle (entreno_id,ejercicio_id,nro_serie,reps,peso) VALUES
(1,1,1,5,80.0),(1,1,2,5,80.0);
