package ar.edu.gymcontrol.repository;

import ar.edu.gymcontrol.model.Socio;
import java.util.*;

public class SocioRepoInMemory implements SocioRepository {
    private final Map<String,Socio> data = new HashMap<>();

    @Override public Optional<Socio> findByDni(String dni){ return Optional.ofNullable(data.get(dni)); }
    @Override public List<Socio> findAll(){ return new ArrayList<>(data.values()); } // copia (encapsulamiento)
    @Override public void save(Socio s){ data.put(s.getDni(), s); }
    @Override public void deleteByDni(String dni){ data.remove(dni); }
}
