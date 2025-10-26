package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.algorithms.BinarySearch;
import ar.edu.gymcontrol.algorithms.InsertionSort;
import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.repository.SocioRepo;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ServicioReportes {

    private final SocioRepo socios;
    public ServicioReportes(SocioRepo socios) { this.socios = socios; }

    public List<Socio> morososOrdenados() {
        List<Socio> morosos = socios.findAll().stream()
                .filter(s -> s.getCuotaHasta().isBefore(LocalDate.now()))
                .collect(Collectors.toCollection(ArrayList::new));
        InsertionSort.sort(morosos, Comparator
                .comparing(Socio::getApellido, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Socio::getNombre, String.CASE_INSENSITIVE_ORDER));
        return morosos;
    }

    public List<Socio> sociosOrdenadosPorDni() {
        List<Socio> list = new ArrayList<>(socios.findAll());
        InsertionSort.sort(list, Comparator.comparing(Socio::getDni));
        return list;
    }

    public Optional<Socio> buscarPorDniBinaria(String dni) {
        List<Socio> sorted = sociosOrdenadosPorDni();
        int idx = BinarySearch.indexOf(sorted, null, (a, ignored) -> a.getDni().compareTo(dni));
        return (idx >= 0) ? Optional.of(sorted.get(idx)) : Optional.empty();
    }
}
