package ar.edu.gymcontrol.repository;

import ar.edu.gymcontrol.model.Ejercicio;
import java.util.*;

public class EjercicioRepo {
    private final Map<String, Ejercicio> data = new HashMap<>();
    public void save(Ejercicio e){ data.put(e.getId(), e); }
    public List<Ejercicio> findAll(){ return new ArrayList<>(data.values()); }
    public Optional<Ejercicio> findById(String id){ return Optional.ofNullable(data.get(id)); }
    public void deleteById(String id) { data.remove(id); }


}
