package dao;

import config.DatabaseConnection;
import entities.Mascota;
import entities.Microchip;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Mascota
 * Implementa operaciones básicas de base de datos
 * 
 * @author Sistema
 */
public class MascotaDAO {
    
    /**
     * Obtiene todas las mascotas de la base de datos
     * Incluye la información del microchip si está disponible
     * 
     * @return List<Mascota> - Lista de todas las mascotas
     * @throws SQLException si ocurre un error en la consulta
     */
    public List<Mascota> obtenerTodas() throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        
        String sql = "SELECT " +
                     "m.id_mascota as id_mascota, " +
                     "m.eliminado as mascota_eliminado, " +
                     "m.nombre, " +
                     "m.especie, " +
                     "m.raza, " +
                     "m.fecha_nacimiento, " +
                     "m.duenio, " +
                     "mc.id_microchip as id_microchip, " +
                     "mc.eliminado as microchip_eliminado, " +
                     "mc.codigo, " +
                     "mc.fecha_implantacion, " +
                     "mc.veterinaria, " +
                     "mc.observaciones " +
                     "FROM mascota m " +
                     "LEFT JOIN microchip mc ON m.id_mascota = mc.id_mascota " +
                     "WHERE m.eliminado = FALSE " +
                     "ORDER BY m.nombre LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Crear mascota
                Mascota mascota = new Mascota();
                mascota.setId(rs.getLong("id_mascota"));
                mascota.setEliminado(rs.getBoolean("mascota_eliminado"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                
                // Manejar fecha de nacimiento (puede ser null)
                java.sql.Date fechaNac = rs.getDate("fecha_nacimiento");
                if (fechaNac != null) {
                    mascota.setFechaNacimiento(fechaNac.toLocalDate());
                }
                
                mascota.setDuenio(rs.getString("duenio"));
                
                // Crear microchip si existe
                Long microchipId = rs.getLong("id_microchip");
                if (microchipId != 0 && !rs.wasNull()) { // Si tiene microchip
                    Microchip microchip = new Microchip();
                    microchip.setId(microchipId);
                    microchip.setEliminado(rs.getBoolean("microchip_eliminado"));
                    microchip.setCodigo(rs.getString("codigo"));
                    
                    // Manejar fecha de implantación (puede ser null)
                    java.sql.Date fechaImpl = rs.getDate("fecha_implantacion");
                    if (fechaImpl != null) {
                        microchip.setFechaImplantacion(fechaImpl.toLocalDate());
                    }
                    
                    microchip.setVeterinaria(rs.getString("veterinaria"));
                    microchip.setObservaciones(rs.getString("observaciones"));
                    
                    mascota.setMicrochip(microchip);
                }
                
                mascotas.add(mascota);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mascotas: " + e.getMessage());
            throw e;
        }
        
        return mascotas;
    }
    
    /**
     * Obtiene una mascota por su ID
     * 
     * @param id ID de la mascota a buscar
     * @return Mascota encontrada o null si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    public Mascota obtenerPorId(Long id) throws SQLException {
        String sql = "SELECT " +
                     "m.id_mascota as id_mascota, " +
                     "m.eliminado as mascota_eliminado, " +
                     "m.nombre, " +
                     "m.especie, " +
                     "m.raza, " +
                     "m.fecha_nacimiento, " +
                     "m.duenio, " +
                     "mc.id_microchip as id_microchip, " +
                     "mc.eliminado as microchip_eliminado, " +
                     "mc.codigo, " +
                     "mc.fecha_implantacion, " +
                     "mc.veterinaria, " +
                     "mc.observaciones " +
                     "FROM mascota m " +
                     "LEFT JOIN microchip mc ON m.id_mascota = mc.id_mascota " +
                     "WHERE m.id_mascota = ? AND m.eliminado = FALSE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Crear mascota
                    Mascota mascota = new Mascota();
                    mascota.setId(rs.getLong("id_mascota"));
                    mascota.setEliminado(rs.getBoolean("mascota_eliminado"));
                    mascota.setNombre(rs.getString("nombre"));
                    mascota.setEspecie(rs.getString("especie"));
                    mascota.setRaza(rs.getString("raza"));
                    
                    // Manejar fecha de nacimiento
                    java.sql.Date fechaNac = rs.getDate("fecha_nacimiento");
                    if (fechaNac != null) {
                        mascota.setFechaNacimiento(fechaNac.toLocalDate());
                    }
                    
                    mascota.setDuenio(rs.getString("duenio"));
                    
                    // Crear microchip si existe
                    Long microchipId = rs.getLong("id_microchip");
                    if (microchipId != 0 && !rs.wasNull()) {
                        Microchip microchip = new Microchip();
                        microchip.setId(microchipId);
                        microchip.setEliminado(rs.getBoolean("microchip_eliminado"));
                        microchip.setCodigo(rs.getString("codigo"));
                        
                        java.sql.Date fechaImpl = rs.getDate("fecha_implantacion");
                        if (fechaImpl != null) {
                            microchip.setFechaImplantacion(fechaImpl.toLocalDate());
                        }
                        
                        microchip.setVeterinaria(rs.getString("veterinaria"));
                        microchip.setObservaciones(rs.getString("observaciones"));
                        
                        mascota.setMicrochip(microchip);
                    }
                    
                    return mascota;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener mascota por ID: " + e.getMessage());
            throw e;
        }
        
        return null;
    }
    
    /**
     * Busca mascotas por nombre (búsqueda parcial)
     * 
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de mascotas que coinciden con el criterio
     * @throws SQLException si ocurre un error en la consulta
     */
    public List<Mascota> buscarPorNombre(String nombre) throws SQLException {
        List<Mascota> mascotas = new ArrayList<>();
        
        String sql = "SELECT " +
                     "m.id_mascota as id_mascota, " +
                     "m.eliminado as mascota_eliminado, " +
                     "m.nombre, " +
                     "m.especie, " +
                     "m.raza, " +
                     "m.fecha_nacimiento, " +
                     "m.duenio, " +
                     "mc.id_microchip as id_microchip, " +
                     "mc.eliminado as microchip_eliminado, " +
                     "mc.codigo, " +
                     "mc.fecha_implantacion, " +
                     "mc.veterinaria, " +
                     "mc.observaciones " +
                     "FROM mascota m " +
                     "LEFT JOIN microchip mc ON m.id_mascota = mc.id_mascota " +
                     "WHERE m.nombre LIKE ? AND m.eliminado = FALSE " +
                     "ORDER BY m.nombre LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crear mascota (código similar al método obtenerTodas)
                    Mascota mascota = new Mascota();
                    mascota.setId(rs.getLong("id_mascota"));
                    mascota.setEliminado(rs.getBoolean("mascota_eliminado"));
                    mascota.setNombre(rs.getString("nombre"));
                    mascota.setEspecie(rs.getString("especie"));
                    mascota.setRaza(rs.getString("raza"));
                    
                    java.sql.Date fechaNac = rs.getDate("fecha_nacimiento");
                    if (fechaNac != null) {
                        mascota.setFechaNacimiento(fechaNac.toLocalDate());
                    }
                    
                    mascota.setDuenio(rs.getString("duenio"));
                    
                    // Crear microchip si existe
                    Long microchipId = rs.getLong("id_microchip");
                    if (microchipId != 0 && !rs.wasNull()) {
                        Microchip microchip = new Microchip();
                        microchip.setId(microchipId);
                        microchip.setEliminado(rs.getBoolean("microchip_eliminado"));
                        microchip.setCodigo(rs.getString("codigo"));
                        
                        java.sql.Date fechaImpl = rs.getDate("fecha_implantacion");
                        if (fechaImpl != null) {
                            microchip.setFechaImplantacion(fechaImpl.toLocalDate());
                        }
                        
                        microchip.setVeterinaria(rs.getString("veterinaria"));
                        microchip.setObservaciones(rs.getString("observaciones"));
                        
                        mascota.setMicrochip(microchip);
                    }
                    
                    mascotas.add(mascota);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar mascotas por nombre: " + e.getMessage());
            throw e;
        }
        
        return mascotas;
    }
    
    /**
     * Cuenta el total de mascotas activas en la base de datos
     * 
     * @return Número total de mascotas activas
     * @throws SQLException si ocurre un error en la consulta
     */
    public int contarMascotas() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM mascota WHERE eliminado = FALSE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar mascotas: " + e.getMessage());
            throw e;
        }
        
        return 0;
    }
}