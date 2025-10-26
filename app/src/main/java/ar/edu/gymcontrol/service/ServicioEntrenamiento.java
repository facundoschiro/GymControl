package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.model.*;
import ar.edu.gymcontrol.repository.EjercicioRepo;
import ar.edu.gymcontrol.repository.SesionRepo;

import java.time.LocalDate;
import java.util.*;

public class ServicioEntrenamiento {
    private final SesionRepo sesiones;
    private final EjercicioRepo ejercicios;

    private final Map<String, Deque<Serie>> undoStacks = new HashMap<>();

    public ServicioEntrenamiento(SesionRepo sesiones, EjercicioRepo ejercicios) {
        this.sesiones = sesiones;
        this.ejercicios = ejercicios;
    }

    public SesionEntrenamiento abrirSesion(String socioDni, LocalDate fecha) {
        return sesiones.findBySocioAndFecha(socioDni, fecha).orElseGet(() -> {
            SesionEntrenamiento s = new SesionEntrenamiento(socioDni, fecha);
            sesiones.save(s);
            undoStacks.put(s.getId(), new ArrayDeque<>());
            return s;
        });
    }

    public SesionEntrenamiento agregarSerie(String sesionId, String ejercicioId, int reps, double peso) {
        SesionEntrenamiento s = sesiones.findById(sesionId).orElseThrow();
        Ejercicio ej = ejercicios.findById(ejercicioId).orElseThrow();
        int nro = s.getSeries().size() + 1;
        Serie serie = new Serie(nro, ej, reps, peso);
        s.addSerie(serie);
        undoStacks.get(sesionId).push(serie);
        return s;
    }

    public Optional<Serie> deshacerUltima(String sesionId) {
        SesionEntrenamiento s = sesiones.findById(sesionId).orElseThrow();
        Serie last = s.removeLastSerie();
        if (last != null) undoStacks.get(sesionId).poll(); // descartar del stack
        return Optional.ofNullable(last);
    }
}
