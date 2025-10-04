SELECT DATE(fecha_hora) AS dia,
       HOUR(fecha_hora) AS hora,
       COUNT(*) AS ingresos
FROM acceso
WHERE resultado = 'PERMITIDO'
GROUP BY DATE(fecha_hora), HOUR(fecha_hora)
ORDER BY dia, hora;
