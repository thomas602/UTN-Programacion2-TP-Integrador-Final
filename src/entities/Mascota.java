package entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad Mascota - Clase A en relación 1→1 unidireccional
 * Representa una mascota con referencia a su microchip
 * 
 * @author Sistema
 */
public class Mascota {
    
    // Clave primaria
    private Long id;
    
    // Baja lógica
    private Boolean eliminado;
    
    // Nombre de la mascota (NOT NULL, máx. 60)
    private String nombre;
    
    // Especie de la mascota (NOT NULL, máx. 30)
    private String especie;
    
    // Raza de la mascota (máx. 60)
    private String raza;
    
    // Fecha de nacimiento
    private LocalDate fechaNacimiento;
    
    // Dueño de la mascota (NOT NULL, máx. 120)
    private String duenio;
    
    // Referencia 1→1 unidireccional a Microchip
    private Microchip microchip;
    
    // Constructor por defecto
    public Mascota() {
        this.eliminado = false;
    }
    
    // Constructor con parámetros obligatorios
    public Mascota(String nombre, String especie, String duenio) {
        this();
        this.nombre = nombre;
        this.especie = especie;
        this.duenio = duenio;
    }
    
    // Constructor completo
    public Mascota(Long id, String nombre, String especie, String raza, 
                  LocalDate fechaNacimiento, String duenio, Microchip microchip) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.duenio = duenio;
        this.microchip = microchip;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean getEliminado() {
        return eliminado;
    }
    
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEspecie() {
        return especie;
    }
    
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    
    public String getRaza() {
        return raza;
    }
    
    public void setRaza(String raza) {
        this.raza = raza;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getDuenio() {
        return duenio;
    }
    
    public void setDuenio(String duenio) {
        this.duenio = duenio;
    }
    
    public Microchip getMicrochip() {
        return microchip;
    }
    
    public void setMicrochip(Microchip microchip) {
        this.microchip = microchip;
    }
    
    // Métodos de validación
    public boolean isValid() {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 60 &&
               especie != null && !especie.trim().isEmpty() && especie.length() <= 30 &&
               duenio != null && !duenio.trim().isEmpty() && duenio.length() <= 120;
    }
    
    // Método para verificar si tiene microchip
    public boolean tieneMicrochip() {
        return microchip != null && !microchip.getEliminado();
    }
    
    // Método para asignar microchip
    public void asignarMicrochip(Microchip microchip) {
        this.microchip = microchip;
    }
    
    // Método para remover microchip
    public void removerMicrochip() {
        this.microchip = null;
    }
    
    // Método para baja lógica
    public void eliminar() {
        this.eliminado = true;
    }
    
    // Método para reactivar
    public void reactivar() {
        this.eliminado = false;
    }
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Mascota mascota = (Mascota) obj;
        return Objects.equals(id, mascota.id) && 
               Objects.equals(nombre, mascota.nombre) && 
               Objects.equals(duenio, mascota.duenio);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, duenio);
    }
    
    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", raza='" + raza + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", duenio='" + duenio + '\'' +
                ", microchip=" + (microchip != null ? microchip.getCodigo() : "null") +
                '}';
    }
}