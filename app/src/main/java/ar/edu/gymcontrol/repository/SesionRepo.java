package ar.edu.gymcontrol.repository;

import ar.edu.gymcontrol.model.SesionEntrenamiento;
import java.time.LocalDate;
import java.util.*;

public class SesionRepo {
    private final Map<String, SesionEntrenamiento> data = new HashMap<>();
    public void save(SesionEntrenamiento s){ data.put(s.getId(), s); }
    public Optional<SesionEntrenamiento> findById(String id){ return Optional.ofNullable(data.get(id)); }

    public Optional<SesionEntrenamiento> findBySocioAndFecha(String dni, LocalDate fecha){
        return data.values().stream()
                .filter(s -> s.getSocioDni().equals(dni) && s.getFecha().equals(fecha))
                .findFirst();
    }
}
