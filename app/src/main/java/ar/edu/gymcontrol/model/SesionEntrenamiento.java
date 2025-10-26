package ar.edu.gymcontrol.model;

import java.time.LocalDate;
import java.util.*;

public class SesionEntrenamiento {
    private final String id = UUID.randomUUID().toString();
    private final String socioDni;
    private final LocalDate fecha;
    private final List<Serie> series = new ArrayList<>();

    public SesionEntrenamiento(String socioDni, LocalDate fecha) {
        this.socioDni = socioDni;
        this.fecha = fecha;
    }
    public String getId() { return id; }
    public String getSocioDni() { return socioDni; }
    public LocalDate getFecha() { return fecha; }
    public List<Serie> getSeries() { return Collections.unmodifiableList(series); }

    public void addSerie(Serie s) { series.add(s); }
    public Serie removeLastSerie() { return series.isEmpty()? null : series.remove(series.size()-1); }

    public double volumenTotal() { return series.stream().mapToDouble(Serie::volumen).sum(); }

    public Optional<Serie> ultimaMarcaPorEjercicio(Ejercicio ej) {
        for (int i = series.size()-1; i>=0; i--) {
            if (series.get(i).getEjercicio().getId().equals(ej.getId())) return Optional.of(series.get(i));
        }
        return Optional.empty();
    }
}
