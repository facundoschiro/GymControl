package ar.edu.gymcontrol.model;

import java.time.LocalDate;
import java.util.Objects;

public class Socio {
    private final String dni;
    private String nombre;
    private String apellido;
    private EstadoSocio estado = EstadoSocio.ACTIVO;
    private LocalDate aptoHasta;    // fecha de vencimiento del apto médico
    private LocalDate cuotaHasta;   // fecha de vigencia de cuota

    public Socio(String dni, String nombre, String apellido, LocalDate aptoHasta, LocalDate cuotaHasta) {
        if (dni == null || !dni.matches("\\d{7,8}")) throw new IllegalArgumentException("DNI inválido");
        this.dni = dni;
        this.nombre = Objects.requireNonNull(nombre);
        this.apellido = Objects.requireNonNull(apellido);
        this.aptoHasta = Objects.requireNonNull(aptoHasta);
        this.cuotaHasta = Objects.requireNonNull(cuotaHasta);
    }

    // Getters/Setters (encapsulamiento)
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public EstadoSocio getEstado() { return estado; }
    public LocalDate getAptoHasta() { return aptoHasta; }
    public LocalDate getCuotaHasta() { return cuotaHasta; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEstado(EstadoSocio estado) { this.estado = estado; }
    public void setAptoHasta(LocalDate aptoHasta) { this.aptoHasta = aptoHasta; }
    public void setCuotaHasta(LocalDate cuotaHasta) { this.cuotaHasta = cuotaHasta; }

    @Override public String toString() { return apellido + ", " + nombre + " (" + dni + ")"; }
}
