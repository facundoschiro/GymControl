package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.repository.SocioRepoInMemory;
import ar.edu.gymcontrol.repository.SocioRepoJdbc;
import ar.edu.gymcontrol.repository.SocioRepository;

public class ServiceRegistry {
    private static ServiceRegistry INSTANCE;

    public enum Persistence { IN_MEMORY, JDBC }

    // --- Repos como interfaces
    public final SocioRepository socioRepo;
    public final ar.edu.gymcontrol.repository.EjercicioRepo ejercicioRepo;
    public final ar.edu.gymcontrol.repository.SesionRepo    sesionRepo;

    public final ServicioAcceso servicioAcceso;
    public final ServicioEntrenamiento servicioEntrenamiento;
    public final ServicioReportes servicioReportes;

    private ServiceRegistry() {
        var mode = Persistence.JDBC; // cambiá cuando quieras

        if (mode == Persistence.JDBC) {
            socioRepo   = new SocioRepoJdbc();
            ejercicioRepo = new ar.edu.gymcontrol.repository.EjercicioRepo(); // in-memory por ahora
            sesionRepo    = new ar.edu.gymcontrol.repository.SesionRepo();    // in-memory por ahora
        } else {
            socioRepo   = new SocioRepoInMemory();
            ejercicioRepo = new ar.edu.gymcontrol.repository.EjercicioRepo();
            sesionRepo    = new ar.edu.gymcontrol.repository.SesionRepo();
            ar.edu.gymcontrol.bootstrap.BootstrapData.load(socioRepo); // acepta la interfaz
        }

        var validators = java.util.List.of(
                new ar.edu.gymcontrol.service.validador.ValidadorEstado(),
                new ar.edu.gymcontrol.service.validador.ValidadorApto(),
                new ar.edu.gymcontrol.service.validador.ValidadorCuota()
        );

        servicioAcceso        = new ServicioAcceso(socioRepo, validators); // usa la interfaz
        servicioEntrenamiento = new ServicioEntrenamiento(sesionRepo, ejercicioRepo);
        servicioReportes      = new ServicioReportes(socioRepo);
    }

    public static ServiceRegistry get() {
        if (INSTANCE == null) INSTANCE = new ServiceRegistry();
        return INSTANCE;
    }
}
