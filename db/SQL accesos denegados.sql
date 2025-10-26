SELECT motivo, COUNT(*) AS cantidad
FROM acceso
WHERE resultado = 'DENEGADO'
GROUP BY motivo
ORDER BY cantidad DESC;
