package ar.edu.gymcontrol.model;

import java.util.Objects;
import java.util.UUID;

public class Ejercicio {
    private final String id = UUID.randomUUID().toString();
    private String nombre;
    private GrupoMuscular grupo;

    public Ejercicio(String nombre, GrupoMuscular grupo) {
        this.nombre = Objects.requireNonNull(nombre);
        this.grupo = Objects.requireNonNull(grupo);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public GrupoMuscular getGrupo() { return grupo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setGrupo(GrupoMuscular grupo) { this.grupo = grupo; }

    @Override public String toString() { return nombre + " (" + grupo + ")"; }
}
