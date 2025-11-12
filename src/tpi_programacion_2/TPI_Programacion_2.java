/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpi_programacion_2;

import config.DatabaseConnection;
import dao.MascotaDAO;
import entities.Mascota;
import entities.Microchip;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author thoma
 */
public class TPI_Programacion_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== TP INTEGRADOR PROGRAMACIÓN 2 ===");
        System.out.println("Prueba de SELECT de Mascotas\n");
        
        // Probar conexión a base de datos
        System.out.println("1. PROBANDO CONEXIÓN A BASE DE DATOS:");
        DatabaseConnection.testConnection();
        System.out.println();
        
        // Probar SELECT de mascotas
        probarSelectMascotas();
    }
    
    /**
     * Método para probar las consultas SELECT de mascotas
     */
    public static void probarSelectMascotas() {
        MascotaDAO mascotaDAO = new MascotaDAO();
        
        try {
            System.out.println("2. PROBANDO SELECT DE TODAS LAS MASCOTAS:");
            System.out.println("==========================================");
            
            // Obtener todas las mascotas
            List<Mascota> mascotas = mascotaDAO.obtenerTodas();
            
            if (mascotas.isEmpty()) {
                System.out.println("❌ No se encontraron mascotas en la base de datos.");
                System.out.println("💡 Verifica que hayas ejecutado el script insert_test_data.sql");
                return;
            }
            
            System.out.println("✅ Se encontraron " + mascotas.size() + " mascotas:");
            System.out.println();
            
            // Mostrar información de cada mascota
            int contador = 1;
            for (Mascota mascota : mascotas) {
                System.out.println("--- MASCOTA #" + contador + " ---");
                System.out.println("ID: " + mascota.getId());
                System.out.println("Nombre: " + mascota.getNombre());
                System.out.println("Especie: " + mascota.getEspecie());
                System.out.println("Raza: " + (mascota.getRaza() != null ? mascota.getRaza() : "No especificada"));
                System.out.println("Fecha Nacimiento: " + (mascota.getFechaNacimiento() != null ? mascota.getFechaNacimiento() : "No especificada"));
                System.out.println("Dueño: " + mascota.getDuenio());
                
                // Mostrar información del microchip si existe
                Microchip microchip = mascota.getMicrochip();
                if (microchip != null) {
                    System.out.println("🔸 MICROCHIP:");
                    System.out.println("  - Código: " + microchip.getCodigo());
                    System.out.println("  - Fecha Implantación: " + (microchip.getFechaImplantacion() != null ? microchip.getFechaImplantacion() : "No especificada"));
                    System.out.println("  - Veterinaria: " + (microchip.getVeterinaria() != null ? microchip.getVeterinaria() : "No especificada"));
                    if (microchip.getObservaciones() != null && !microchip.getObservaciones().trim().isEmpty()) {
                        System.out.println("  - Observaciones: " + microchip.getObservaciones());
                    }
                } else {
                    System.out.println("🔸 Sin microchip asignado");
                }
                
                System.out.println();
                contador++;
            }
            
            // Probar otras consultas
            System.out.println("\n3. PROBANDO OTRAS CONSULTAS:");
            System.out.println("============================");
            
            // Contar mascotas
            int total = mascotaDAO.contarMascotas();
            System.out.println("✅ Total de mascotas activas: " + total);
            
            // Buscar una mascota por ID (probamos con la primera)
            if (!mascotas.isEmpty()) {
                Long idPrueba = mascotas.get(0).getId();
                System.out.println("\n🔍 Buscando mascota por ID (" + idPrueba + "):");
                Mascota mascotaPorId = mascotaDAO.obtenerPorId(idPrueba);
                if (mascotaPorId != null) {
                    System.out.println("✅ Mascota encontrada: " + mascotaPorId.getNombre() + " (" + mascotaPorId.getEspecie() + ")");
                } else {
                    System.out.println("❌ No se encontró mascota con ID " + idPrueba);
                }
                
                // Buscar mascotas por nombre parcial
                String nombreBusqueda = "a"; // Buscar mascotas que contengan "a"
                System.out.println("\n🔍 Buscando mascotas que contengan '" + nombreBusqueda + "':");
                List<Mascota> mascotasEncontradas = mascotaDAO.buscarPorNombre(nombreBusqueda);
                System.out.println("✅ Se encontraron " + mascotasEncontradas.size() + " mascotas:");
                for (Mascota m : mascotasEncontradas) {
                    System.out.println("  - " + m.getNombre() + " (" + m.getEspecie() + ")");
                }
            }
            
            System.out.println("\n✅ ¡TODAS LAS PRUEBAS DE SELECT COMPLETADAS EXITOSAMENTE! ✅");
            
        } catch (SQLException e) {
            System.err.println("❌ ERROR AL EJECUTAR CONSULTAS:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Código de error: " + e.getErrorCode());
            System.err.println("   Estado SQL: " + e.getSQLState());
            
            System.err.println("\n🔧 POSIBLES SOLUCIONES:");
            System.err.println("   1. Verifica que MySQL esté ejecutándose");
            System.err.println("   2. Confirma que la base de datos existe");
            System.err.println("   3. Verifica las credenciales en DatabaseConnection");
            System.err.println("   4. Asegúrate de haber ejecutado create_database.sql");
            System.err.println("   5. Ejecuta insert_test_data.sql para tener datos de prueba");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR INESPERADO:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar conexión
            DatabaseConnection.closeConnection();
            System.out.println("\n🔌 Conexión cerrada.");
        }
    }
}
