package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.exceptions.SocioNoEncontradoException;
import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.repository.SocioRepoInMemory;
import ar.edu.gymcontrol.repository.SocioRepository;
import ar.edu.gymcontrol.service.validador.ValidadorSocio;

import java.util.List;

public class ServicioAcceso {
    private final SocioRepository socios;
    private final java.util.List<ValidadorSocio> validadores;

    public ServicioAcceso(SocioRepository socios, java.util.List<ValidadorSocio> validadores) {
        this.socios = socios;
        this.validadores = validadores;
    }

    public ValidacionAcceso validarPorDni(String dni) {
        try {
            Socio s = socios.findByDni(dni).orElseThrow(() -> new SocioNoEncontradoException(dni));
            for (ValidadorSocio v : validadores) { v.validar(s); }   // <- POLIMORFISMO
            return new ValidacionAcceso(true, "Acceso permitido a " + s);
        } catch (RuntimeException ex) {
            return new ValidacionAcceso(false, ex.getMessage());
        }
    }
}
