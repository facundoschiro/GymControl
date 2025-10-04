-- Morosos
SET @gracia := 3;

SELECT s.id, s.dni, s.nombre
FROM socio s
WHERE s.estado = 'ACTIVO'
  AND NOT EXISTS (
      SELECT 1
      FROM cuota c
      WHERE c.socio_id = s.id
        AND CURDATE() BETWEEN c.periodo_desde AND DATE_ADD(c.periodo_hasta, INTERVAL @gracia DAY)
        AND c.estado IN ('VIGENTE','PENDIENTE')
  )
ORDER BY s.nombre;
