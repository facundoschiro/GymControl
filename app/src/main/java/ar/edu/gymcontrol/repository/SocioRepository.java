package ar.edu.gymcontrol.repository;

import ar.edu.gymcontrol.model.Socio;
import java.util.*;

public interface SocioRepository {
    Optional<Socio> findByDni(String dni);
    List<Socio> findAll();
    void save(Socio s);
    void deleteByDni(String dni);
}
