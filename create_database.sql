-- =============================================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS - TP INTEGRADOR PROGRAMACIÓN 2
-- Base de datos: mascotasdb
-- Relación 1→1 unidireccional: Mascota → Microchip
-- =============================================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS mascotasdb
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE mascotasdb;

-- =============================================================================
-- TABLA: microchips (Clase B - Referenciada)
-- Esta tabla debe crearse PRIMERO ya que será referenciada por mascotas
-- =============================================================================

CREATE TABLE microchip (
    id_microchip BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    codigo VARCHAR(25) NOT NULL UNIQUE,
    fecha_implantacion DATE,
    veterinaria VARCHAR(120),
    observaciones VARCHAR(255),
    
    -- Índices para optimizar consultas
    INDEX idx_microchips_eliminado (eliminado),
    INDEX idx_microchips_codigo (codigo),
    INDEX idx_microchips_fecha (fecha_implantacion)
) ENGINE=InnoDB 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci
  COMMENT='Tabla de microchips de identificación para mascotas';

-- =============================================================================
-- TABLA: mascotas (Clase A - Referenciante)
-- Contiene la referencia 1→1 unidireccional hacia microchips
-- =============================================================================

CREATE TABLE mascota (
    id_mascota BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    nombre VARCHAR(60) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(60),
    fecha_nacimiento DATE,
    duenio VARCHAR(120) NOT NULL,
    microchip_id BIGINT UNIQUE, -- Relación 1→1 con restricción UNIQUE
    
    -- Clave foránea con restricciones de integridad referencial
    CONSTRAINT fk_mascotas_microchip 
        FOREIGN KEY (microchip_id) 
        REFERENCES microchip(id_microchip) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    -- Índices para optimizar consultas
    INDEX idx_mascotas_eliminado (eliminado),
    INDEX idx_mascotas_nombre (nombre),
    INDEX idx_mascotas_especie (especie),
    INDEX idx_mascotas_duenio (duenio),
    INDEX idx_mascotas_fecha_nacimiento (fecha_nacimiento),
    
    -- Índice único para garantizar la relación 1→1
    UNIQUE INDEX uk_mascotas_microchip (microchip_id)
) ENGINE=InnoDB 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci
  COMMENT='Tabla de mascotas con referencia 1→1 a microchips';

-- =============================================================================
-- RESTRICCIONES ADICIONALES Y VALIDACIONES
-- =============================================================================

-- Agregar restricciones CHECK para validar datos (MySQL 8.0+)
-- Nota: En versiones anteriores a MySQL 8.0, estas validaciones deben hacerse en la aplicación

-- Validación para código de microchip (no vacío)
ALTER TABLE microchip 
ADD CONSTRAINT chk_microchip_codigo 
CHECK (LENGTH(TRIM(codigo)) > 0);

-- Validación para nombre de mascota (no vacío)
ALTER TABLE mascota 
ADD CONSTRAINT chk_mascota_nombre 
CHECK (LENGTH(TRIM(nombre)) > 0);

-- Validación para especie de mascota (no vacío)
ALTER TABLE mascota 
ADD CONSTRAINT chk_mascota_especie 
CHECK (LENGTH(TRIM(especie)) > 0);

-- Validación para dueño de mascota (no vacío)
ALTER TABLE mascota 
ADD CONSTRAINT chk_mascota_duenio 
CHECK (LENGTH(TRIM(duenio)) > 0);

-- =============================================================================
-- INFORMACIÓN ADICIONAL
-- =============================================================================

-- Mostrar información de las tablas creadas
SHOW TABLES;

-- Describir estructura de las tablas
DESCRIBE microchip;
DESCRIBE mascota;

-- Mostrar las claves foráneas creadas
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'mascotasdb'
ORDER BY TABLE_NAME;

-- =============================================================================
-- COMENTARIOS SOBRE EL DISEÑO
-- =============================================================================

/*
DISEÑO DE RELACIÓN 1→1 UNIDIRECCIONAL:

1. TABLA microchip:
   - Tabla independiente que puede existir sin mascotas
   - Código único para cada microchip
   - Campos de auditoría (eliminado) para baja lógica
   - PK: id_microchip

2. TABLA mascota:
   - Contiene la referencia FK hacia microchip
   - microchip_id es UNIQUE para garantizar relación 1→1
   - ON DELETE CASCADE: Si se elimina un microchip, la referencia en mascota se pone NULL
   - ON UPDATE CASCADE: Si cambia el ID del microchip, se actualiza automáticamente
   - PK: id_mascota

3. CARACTERÍSTICAS DE LA RELACIÓN:
   - Una mascota puede tener MÁXIMO un microchip (UNIQUE constraint)
   - Un microchip puede estar asociado a MÁXIMO una mascota (UNIQUE constraint)
   - La relación es UNIDIRECCIONAL: solo mascota conoce su microchip
   - Es posible tener microchips sin asignar a ninguna mascota
   - Es posible tener mascotas sin microchip (microchip_id NULL)

4. VENTAJAS DEL DISEÑO:
   - Flexibilidad: permite microchips no asignados
   - Integridad referencial garantizada por la base de datos
   - Optimización: índices en campos de búsqueda frecuente
   - Escalabilidad: estructura preparada para grandes volúmenes
*/