SET @socio := 1;
SET @ejercicio := 1;
SET @fecha := '2025-10-10';
WITH volumen AS (
  SELECT d.entreno_id, SUM(d.reps * d.peso) AS volumen_total
  FROM entrenamiento_detalle d
  JOIN entrenamiento e ON e.id = d.entreno_id
  WHERE e.socio_id = @socio
    AND e.fecha = @fecha
    AND d.ejercicio_id = @ejercicio
  GROUP BY d.entreno_id
),
ultima AS (
  SELECT entreno_id, reps, peso
  FROM (
    SELECT d.*,
           ROW_NUMBER() OVER (PARTITION BY d.entreno_id ORDER BY d.nro_serie DESC) AS rn
    FROM entrenamiento_detalle d
    JOIN entrenamiento e ON e.id = d.entreno_id
    WHERE e.socio_id = @socio
      AND e.fecha = @fecha
      AND d.ejercicio_id = @ejercicio
  ) x
  WHERE rn = 1
)
SELECT e.fecha,
       v.volumen_total,
       CONCAT(u.reps,'x',u.peso) AS ultima_marca
FROM entrenamiento e
JOIN volumen v ON v.entreno_id = e.id
LEFT JOIN ultima u ON u.entreno_id = e.id
WHERE e.socio_id = @socio
  AND e.fecha = @fecha;
