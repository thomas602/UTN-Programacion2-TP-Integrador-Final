-- =============================================================================
-- SCRIPT DE DATOS DE PRUEBA - TP INTEGRADOR PROGRAMACIÓN 2
-- Base de datos: mascotasdb
-- Datos de ejemplo para mascotas y microchips
-- =============================================================================

-- Usar la base de datos
USE mascotasdb;

-- =============================================================================
-- LIMPIAR DATOS EXISTENTES (OPCIONAL)
-- Descomentar estas líneas si se quiere reiniciar completamente
-- =============================================================================

-- SET FOREIGN_KEY_CHECKS = 0; -- Desactivar verificación de claves foráneas temporalmente
-- TRUNCATE TABLE mascotas;
-- TRUNCATE TABLE microchips;
-- SET FOREIGN_KEY_CHECKS = 1; -- Reactivar verificación de claves foráneas

-- =============================================================================
-- INSERTAR MICROCHIPS DE PRUEBA
-- Se insertan PRIMERO porque son referenciados por las mascotas
-- =============================================================================

INSERT INTO microchips (codigo, fecha_implantacion, veterinaria, observaciones) VALUES
('MC001-ARG-2024', '2024-01-15', 'Veterinaria San Martín', 'Microchip implantado durante vacunación anual'),
('MC002-ARG-2024', '2024-02-03', 'Clínica Veterinaria Central', 'Implantación rutinaria, sin complicaciones'),
('MC003-ARG-2024', '2024-01-28', 'Hospital Veterinario Norte', 'Microchip de emergencia tras extravío'),
('MC004-ARG-2024', '2024-03-10', 'Veterinaria Los Alamos', 'Implantado junto con esterilización'),
('MC005-ARG-2024', '2024-02-14', 'Centro Veterinario Integral', 'Microchip premium con GPS básico'),
('MC006-ARG-2024', '2024-03-05', 'Veterinaria San Martín', 'Reemplazo de microchip dañado'),
('MC007-ARG-2024', '2024-01-20', 'Clínica Dr. Pérez', 'Implantación en cachorro de 3 meses'),
('MC008-ARG-2024', '2024-02-25', 'Hospital Veterinario Sur', 'Microchip internacional para viajes'),

-- Microchips sin asignar (para demostrar flexibilidad del diseño)
('MC009-ARG-2024', '2024-03-12', 'Veterinaria Municipal', 'En stock para próximas adopciones'),
('MC010-ARG-2024', '2024-03-15', 'Centro de Adopción', 'Reservado para gato en recuperación'),
('MC011-ARG-2024', NULL, NULL, 'Microchip nuevo sin implantar'),
('MC012-ARG-2024', NULL, NULL, 'Microchip de respaldo');

-- =============================================================================
-- INSERTAR MASCOTAS DE PRUEBA
-- Algunas con microchip, otras sin microchip para mostrar la relación 1→1
-- =============================================================================

-- Mascotas CON microchip asignado
INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, duenio, microchip_id) VALUES
('Max', 'Perro', 'Labrador Retriever', '2022-05-15', 'Juan Carlos Pérez', 1),
('Luna', 'Gato', 'Siamés', '2021-08-22', 'María González López', 2),
('Rocky', 'Perro', 'Pastor Alemán', '2020-12-03', 'Carlos Alberto Ruiz', 3),
('Michi', 'Gato', 'Persa', '2023-02-14', 'Ana Sofía Martínez', 4),
('Buddy', 'Perro', 'Golden Retriever', '2021-11-30', 'Roberto Miguel Silva', 5),
('Whiskers', 'Gato', 'Maine Coon', '2022-09-18', 'Laura Patricia Fernández', 6),
('Rex', 'Perro', 'Bulldog Francés', '2023-01-07', 'Diego Alejandro Torres', 7),
('Mittens', 'Gato', 'Bengalí', '2022-07-25', 'Carmen Elena Rodríguez', 8);

-- Mascotas SIN microchip asignado (microchip_id = NULL)
INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, duenio, microchip_id) VALUES
('Toby', 'Perro', 'Cocker Spaniel', '2023-06-10', 'Fernando José García', NULL),
('Garfield', 'Gato', 'Naranja Doméstico', '2022-03-20', 'Silvia Beatriz López', NULL),
('Lassie', 'Perro', 'Border Collie', '2021-04-12', 'Andrés Felipe Morales', NULL),
('Felix', 'Gato', 'Negro Doméstico', '2023-08-05', 'Patricia Isabel Herrera', NULL),
('Beethoven', 'Perro', 'San Bernardo', '2020-10-30', 'Miguel Ángel Vargas', NULL),

-- Mascotas de especies diferentes para mostrar diversidad
('Piolín', 'Ave', 'Canario', '2023-04-18', 'Rosa María Jiménez', NULL),
('Nemo', 'Pez', 'Pez Payaso', '2024-01-05', 'Luis Eduardo Castro', NULL),
('Bugs', 'Conejo', 'Conejo Enano', '2022-12-12', 'Gabriela Alejandra Vega', NULL);

-- =============================================================================
-- VERIFICACIONES Y CONSULTAS DE CONTROL
-- =============================================================================

-- Mostrar resumen de datos insertados
SELECT 'RESUMEN DE DATOS INSERTADOS' as info;

-- Contar microchips totales
SELECT COUNT(*) as total_microchips FROM microchips WHERE eliminado = FALSE;

-- Contar microchips asignados vs no asignados
SELECT 
    'Microchips asignados' as tipo,
    COUNT(*) as cantidad
FROM microchips m 
INNER JOIN mascotas ma ON m.id = ma.microchip_id 
WHERE m.eliminado = FALSE

UNION ALL

SELECT 
    'Microchips disponibles' as tipo,
    COUNT(*) as cantidad
FROM microchips m 
LEFT JOIN mascotas ma ON m.id = ma.microchip_id 
WHERE m.eliminado = FALSE AND ma.microchip_id IS NULL;

-- Contar mascotas totales
SELECT COUNT(*) as total_mascotas FROM mascotas WHERE eliminado = FALSE;

-- Contar mascotas con y sin microchip
SELECT 
    'Mascotas con microchip' as tipo,
    COUNT(*) as cantidad
FROM mascotas 
WHERE eliminado = FALSE AND microchip_id IS NOT NULL

UNION ALL

SELECT 
    'Mascotas sin microchip' as tipo,
    COUNT(*) as cantidad
FROM mascotas 
WHERE eliminado = FALSE AND microchip_id IS NULL;

-- =============================================================================
-- CONSULTAS DE EJEMPLO PARA VERIFICAR LA RELACIÓN 1→1
-- =============================================================================

-- Consulta 1: Mascotas con su información de microchip (JOIN)
SELECT 
    m.id as mascota_id,
    m.nombre,
    m.especie,
    m.raza,
    m.duenio,
    mc.codigo as codigo_microchip,
    mc.fecha_implantacion,
    mc.veterinaria
FROM mascotas m
LEFT JOIN microchips mc ON m.microchip_id = mc.id
WHERE m.eliminado = FALSE
ORDER BY m.nombre;

-- Consulta 2: Microchips y sus mascotas asignadas
SELECT 
    mc.id as microchip_id,
    mc.codigo,
    mc.fecha_implantacion,
    m.nombre as mascota,
    m.especie,
    m.duenio,
    CASE 
        WHEN m.id IS NULL THEN 'DISPONIBLE'
        ELSE 'ASIGNADO'
    END as estado
FROM microchips mc
LEFT JOIN mascotas m ON mc.id = m.microchip_id
WHERE mc.eliminado = FALSE
ORDER BY mc.codigo;

-- Consulta 3: Verificar que no hay violaciones de la relación 1→1
-- (Esta consulta debe retornar 0 filas si la relación está bien implementada)
SELECT 
    microchip_id,
    COUNT(*) as mascotas_asignadas
FROM mascotas 
WHERE microchip_id IS NOT NULL AND eliminado = FALSE
GROUP BY microchip_id
HAVING COUNT(*) > 1;

-- =============================================================================
-- DATOS ADICIONALES PARA PRUEBAS ESPECÍFICAS
-- =============================================================================

-- Insertar algunas mascotas con fechas de nacimiento nulas para pruebas
INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, duenio, microchip_id) VALUES
('Sombra', 'Gato', 'Callejero', NULL, 'Fundación Adopta una Mascota', NULL),
('Canela', 'Perro', 'Mestizo', NULL, 'Refugio Municipal', NULL);

-- Insertar microchips con fechas de implantación futuras (programadas)
INSERT INTO microchips (codigo, fecha_implantacion, veterinaria, observaciones) VALUES
('MC013-ARG-2024', '2024-12-01', 'Veterinaria Programada', 'Cita programada para implantación'),
('MC014-ARG-2024', '2024-11-25', 'Clínica Futura', 'Pendiente de confirmación de cita');

-- =============================================================================
-- MENSAJE FINAL
-- =============================================================================

SELECT 
    'DATOS DE PRUEBA INSERTADOS EXITOSAMENTE' as mensaje,
    NOW() as fecha_ejecucion;

SELECT 
    'Para verificar los datos, ejecuta las consultas de ejemplo incluidas en este script' as nota;