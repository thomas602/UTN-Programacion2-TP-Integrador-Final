package entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad Microchip - Clase B en relación 1→1 unidireccional
 * Representa un microchip de identificación para mascotas
 * 
 * @author Sistema
 */
public class Microchip {
    
    // Clave primaria
    private Long id;
    
    // Baja lógica
    private Boolean eliminado;
    
    // Código único del microchip (NOT NULL, UNIQUE, máx. 25)
    private String codigo;
    
    // Fecha de implantación del microchip
    private LocalDate fechaImplantacion;
    
    // Veterinaria donde se implantó (máx. 120)
    private String veterinaria;
    
    // Observaciones adicionales (máx. 255)
    private String observaciones;
    
    // Constructor por defecto
    public Microchip() {
        this.eliminado = false;
    }
    
    // Constructor con parámetros obligatorios
    public Microchip(String codigo) {
        this();
        this.codigo = codigo;
    }
    
    // Constructor completo
    public Microchip(Long id, String codigo, LocalDate fechaImplantacion, 
                    String veterinaria, String observaciones) {
        this();
        this.id = id;
        this.codigo = codigo;
        this.fechaImplantacion = fechaImplantacion;
        this.veterinaria = veterinaria;
        this.observaciones = observaciones;
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
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public LocalDate getFechaImplantacion() {
        return fechaImplantacion;
    }
    
    public void setFechaImplantacion(LocalDate fechaImplantacion) {
        this.fechaImplantacion = fechaImplantacion;
    }
    
    public String getVeterinaria() {
        return veterinaria;
    }
    
    public void setVeterinaria(String veterinaria) {
        this.veterinaria = veterinaria;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    // Métodos de validación
    public boolean isValid() {
        return codigo != null && !codigo.trim().isEmpty() && codigo.length() <= 25;
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
        Microchip microchip = (Microchip) obj;
        return Objects.equals(id, microchip.id) && 
               Objects.equals(codigo, microchip.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
    
    @Override
    public String toString() {
        return "Microchip{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", codigo='" + codigo + '\'' +
                ", fechaImplantacion=" + fechaImplantacion +
                ", veterinaria='" + veterinaria + '\'' +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}