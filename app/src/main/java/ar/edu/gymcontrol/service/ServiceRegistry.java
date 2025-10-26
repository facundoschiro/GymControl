package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.bootstrap.BootstrapData;
import ar.edu.gymcontrol.model.Ejercicio;
import ar.edu.gymcontrol.model.GrupoMuscular;
import ar.edu.gymcontrol.repository.EjercicioRepo;
import ar.edu.gymcontrol.repository.SesionRepo;
import ar.edu.gymcontrol.repository.SocioRepo;

public class ServiceRegistry {
    private static ServiceRegistry INSTANCE;

    public final SocioRepo socioRepo;
    public final EjercicioRepo ejercicioRepo;
    public final SesionRepo sesionRepo;
    public final ServicioReportes servicioReportes;

    public final ServicioAcceso servicioAcceso;
    public final ServicioEntrenamiento servicioEntrenamiento;

    private ServiceRegistry() {
        socioRepo = new SocioRepo();
        ejercicioRepo = new EjercicioRepo();
        sesionRepo = new SesionRepo();

        // Datos de prueba
        BootstrapData.load(socioRepo);
        ejercicioRepo.save(new Ejercicio("Press banca",   GrupoMuscular.PECHO));
        ejercicioRepo.save(new Ejercicio("Dominadas",     GrupoMuscular.ESPALDA));
        ejercicioRepo.save(new Ejercicio("Sentadilla",    GrupoMuscular.PIERNAS));
        ejercicioRepo.save(new Ejercicio("Press militar", GrupoMuscular.HOMBROS));
        ejercicioRepo.save(new Ejercicio("Curl bíceps",   GrupoMuscular.BRAZOS));
        ejercicioRepo.save(new Ejercicio("Plancha",       GrupoMuscular.CORE));

        // Servicios
        servicioAcceso = new ServicioAcceso(socioRepo);
        servicioEntrenamiento = new ServicioEntrenamiento(sesionRepo, ejercicioRepo);
        servicioReportes = new ServicioReportes(socioRepo);
    }

    public static ServiceRegistry get() {
        if (INSTANCE == null) INSTANCE = new ServiceRegistry();
        return INSTANCE;
    }
}
