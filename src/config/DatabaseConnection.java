package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para manejar la conexión a la base de datos MySQL
 * Implementa el patrón Singleton para gestionar una única conexión
 * Lee la configuración desde database.properties
 * 
 * @author Sistema
 */
public class DatabaseConnection {
    
    // Parámetros de conexión a la base de datos
    private static String URL = "jdbc:mysql://localhost:3306/tfi_bd";
    private static String USUARIO = "root";
    private static String PASSWORD = "12345"; // Se carga desde database.properties
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Configuración cargada desde archivo
    private static boolean configLoaded = false;
    
    // Instancia única de conexión (Singleton)
    private static Connection conexion = null;
    
    // Constructor privado para evitar instanciación
    public DatabaseConnection() {
        // Constructor privado
    }
    
    /**
     * Carga la configuración desde el archivo database.properties
     */
    private static void loadConfiguration() {
        if (!configLoaded) {
            Properties props = new Properties();
            try (InputStream input = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                
                if (input != null) {
                    props.load(input);
                    
                    // Cargar parámetros desde el archivo
                    URL = props.getProperty("db.url", URL);
                    USUARIO = props.getProperty("db.username", USUARIO);
                    PASSWORD = props.getProperty("db.password", PASSWORD);
                    DRIVER = props.getProperty("db.driver", DRIVER);
                    
                    System.out.println("Configuración cargada desde database.properties");
                } else {
                    System.out.println("Archivo database.properties no encontrado. Usando configuración por defecto.");
                }
                
                configLoaded = true;
                
            } catch (IOException e) {
                System.err.println("Error al cargar configuración: " + e.getMessage());
                System.out.println("Usando configuración por defecto.");
                configLoaded = true;
            }
        }
    }
    
    /**
     * Método estático que retorna una conexión a la base de datos
     * Implementa Singleton para reutilizar la misma conexión
     * 
     * @return Connection - Conexión a la base de datos MySQL
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar configuración si no se ha cargado
            loadConfiguration();
            
            // Cargar el driver de MySQL
            Class.forName(DRIVER);
            
            // Si la conexión es null o está cerrada, crear una nueva
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión a MySQL establecida exitosamente");
                System.out.println("Base de datos: " + conexion.getMetaData().getDatabaseProductName());
            }
            
            return conexion;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new SQLException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Método para cerrar la conexión a la base de datos
     */
    public static void closeConnection() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;
                System.out.println("Conexión a MySQL cerrada exitosamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    /**
     * Método para verificar si la conexión está activa
     * 
     * @return boolean - true si la conexión está activa, false en caso contrario
     */
    public static boolean isConnectionActive() {
        try {
            return conexion != null && !conexion.isClosed() && conexion.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Método para probar la conexión a la base de datos
     * Útil para verificar que los parámetros de conexión sean correctos
     */
    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✓ Prueba de conexión exitosa");
                System.out.println("✓ Base de datos: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("✓ Versión: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("✓ URL: " + conn.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.err.println("✗ Error en la prueba de conexión:");
            System.err.println("  - Mensaje: " + e.getMessage());
            System.err.println("  - Código de error: " + e.getErrorCode());
            System.err.println("  - Estado SQL: " + e.getSQLState());
            System.err.println("\nVerifica que:");
            System.err.println("  1. MySQL esté ejecutándose");
            System.err.println("  2. La base de datos 'mascotasdb' exista");
            System.err.println("  3. Las credenciales sean correctas");
            System.err.println("  4. El driver MySQL esté en el classpath");
        }
    }
}