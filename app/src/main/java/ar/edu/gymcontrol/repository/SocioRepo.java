package ar.edu.gymcontrol.repository;

import ar.edu.gymcontrol.model.Socio;
import java.util.*;

public class SocioRepo {
    private final Map<String, Socio> data = new HashMap<>();

    public Optional<Socio> findByDni(String dni) { return Optional.ofNullable(data.get(dni)); }
    public void save(Socio s) { data.put(s.getDni(), s); }
    public List<Socio> findAll() { return new ArrayList<>(data.values()); }
    public void deleteByDni(String dni) { data.remove(dni); }


}
