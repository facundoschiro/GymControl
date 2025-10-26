package ar.edu.gymcontrol.bootstrap;

import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.repository.SocioRepo;

import java.time.LocalDate;

public final class BootstrapData {
    private BootstrapData() {}

    public static void load(SocioRepo repo) {
        LocalDate hoy = LocalDate.now();
        repo.save(new Socio("20111222","Ana","García",  hoy.plusMonths(6), hoy.plusDays(10)));
        repo.save(new Socio("22123456","Luis","Pérez",   hoy.minusDays(1),  hoy.plusDays(5)));
        repo.save(new Socio("30999888","Marta","Ríos",   hoy.plusMonths(3), hoy.minusDays(2)));
        repo.save(new Socio("28123456","Juan","Luna",    hoy.plusMonths(1), hoy.plusMonths(1)));
        repo.save(new Socio("27987654","Sofía","Chávez", hoy.plusMonths(2), hoy.plusDays(1)));
    }
}
