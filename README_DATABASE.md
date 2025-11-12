# TP Integrador Programación 2 - Configuración de Base de Datos MySQL

## 📋 Descripción General

Este proyecto implementa una relación **1→1 unidireccional** entre las entidades `Mascota` y `Microchip` usando Java con JDBC y MySQL, siguiendo el patrón DAO.

### 🏗️ Estructura de la Relación

- **Mascota** (Clase A) → contiene referencia a **Microchip** (Clase B)
- Relación unidireccional: solo `Mascota` conoce su `Microchip`
- Una mascota puede tener máximo un microchip
- Un microchip puede estar asignado a máximo una mascota

## 🔧 Configuración Inicial

### 1. Instalar MySQL

```bash
# Descargar desde: https://dev.mysql.com/downloads/mysql/
# Instalar MySQL Community Server
# Recordar la contraseña del usuario root
```

### 2. Descargar Driver JDBC

```bash
# Descargar MySQL Connector/J desde:
# https://mvnrepository.com/artifact/com.mysql/mysql-connector-j/8.0.33
# Agregar el JAR al classpath del proyecto
```

### 3. Configurar Credenciales

Editar el archivo `database.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/mascotasdb
db.username=root
db.password=TU_PASSWORD_AQUI
db.driver=com.mysql.cj.jdbc.Driver
```

## 🗄️ Creación de la Base de Datos

### 1. Ejecutar Script de Creación

```sql
-- Ejecutar en MySQL Workbench o desde línea de comandos
source create_database.sql;
```

### 2. Insertar Datos de Prueba

```sql
-- Ejecutar después del script anterior
source insert_test_data.sql;
```

### 3. Verificar Instalación

```java
// En tu aplicación Java
DatabaseConnection.testConnection();
```

## 📊 Estructura de Base de Datos

### Tabla `microchips` (Clase B)

```sql
CREATE TABLE microchips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    codigo VARCHAR(25) NOT NULL UNIQUE,
    fecha_implantacion DATE,
    veterinaria VARCHAR(120),
    observaciones VARCHAR(255)
);
```

### Tabla `mascotas` (Clase A)

```sql
CREATE TABLE mascotas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    nombre VARCHAR(60) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(60),
    fecha_nacimiento DATE,
    duenio VARCHAR(120) NOT NULL,
    microchip_id BIGINT UNIQUE,

    CONSTRAINT fk_mascotas_microchip
        FOREIGN KEY (microchip_id)
        REFERENCES microchips(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
```

## 🔍 Características de la Relación 1→1

### ✅ Garantías Implementadas

- **UNIQUE constraint** en `microchip_id`: una mascota solo puede tener un microchip
- **Foreign Key constraint**: integridad referencial garantizada
- **ON DELETE CASCADE**: si se elimina microchip, se libera la referencia
- **Baja lógica**: campo `eliminado` para soft delete

### 📋 Validaciones

- Código de microchip obligatorio y único
- Nombre, especie y dueño de mascota obligatorios
- Longitudes máximas respetadas en todos los campos
- Fechas con tipo `DATE` para precisión

## 🚀 Uso de las Clases

### Conexión a Base de Datos

```java
// Obtener conexión
Connection conn = DatabaseConnection.getConnection();

// Probar conexión
DatabaseConnection.testConnection();

// Cerrar conexión
DatabaseConnection.closeConnection();
```

### Crear Entidades

```java
// Crear microchip
Microchip microchip = new Microchip("MC001-ARG-2024");
microchip.setFechaImplantacion(LocalDate.now());
microchip.setVeterinaria("Veterinaria Central");

// Crear mascota
Mascota mascota = new Mascota("Max", "Perro", "Juan Pérez");
mascota.setRaza("Labrador");
mascota.setFechaNacimiento(LocalDate.of(2022, 5, 15));

// Asignar microchip a mascota (relación 1→1)
mascota.setMicrochip(microchip);
```

## 📁 Archivos Incluidos

### 📄 Clases Java

- `src/config/DatabaseConnection.java` - Gestión de conexiones
- `src/entities/Mascota.java` - Entidad Mascota (Clase A)
- `src/entities/Microchip.java` - Entidad Microchip (Clase B)

### 🗃️ Scripts SQL

- `create_database.sql` - Creación de BD y tablas
- `insert_test_data.sql` - Datos de prueba
- `database.properties` - Configuración de conexión

## 🔍 Consultas de Ejemplo

### Mascotas con su Microchip

```sql
SELECT
    m.nombre,
    m.especie,
    m.duenio,
    mc.codigo as microchip,
    mc.veterinaria
FROM mascotas m
LEFT JOIN microchips mc ON m.microchip_id = mc.id
WHERE m.eliminado = FALSE;
```

### Microchips Disponibles

```sql
SELECT
    mc.codigo,
    mc.fecha_implantacion,
    CASE
        WHEN m.id IS NULL THEN 'DISPONIBLE'
        ELSE 'ASIGNADO'
    END as estado
FROM microchips mc
LEFT JOIN mascotas m ON mc.id = m.microchip_id
WHERE mc.eliminado = FALSE;
```

### Verificar Integridad 1→1

```sql
-- Esta consulta debe retornar 0 filas
SELECT
    microchip_id,
    COUNT(*) as mascotas_asignadas
FROM mascotas
WHERE microchip_id IS NOT NULL
GROUP BY microchip_id
HAVING COUNT(*) > 1;
```

## 🛠️ Troubleshooting

### Error: Access Denied

```
Causa: Credenciales incorrectas
Solución: Verificar usuario/contraseña en database.properties
```

### Error: Unknown Database

```
Causa: Base de datos no existe
Solución: Ejecutar create_database.sql
```

### Error: Driver Not Found

```
Causa: MySQL Connector no está en classpath
Solución: Agregar mysql-connector-java-x.x.x.jar al proyecto
```

### Error: Communications Link Failure

```
Causa: MySQL no está ejecutándose
Solución: Iniciar servicio MySQL en Windows Services
```

## 📞 Próximos Pasos

1. ✅ Entidades creadas con relación 1→1
2. ✅ Base de datos configurada
3. ⏳ **Siguiente**: Implementar clases DAO
4. ⏳ **Siguiente**: Crear capa Service
5. ⏳ **Siguiente**: Manejo de excepciones
6. ⏳ **Siguiente**: Testing unitario

---

### 🔗 Enlaces Útiles

- [MySQL Downloads](https://dev.mysql.com/downloads/)
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [NetBeans MySQL Setup](https://netbeans.apache.org/tutorials/mysql-nb.html)
