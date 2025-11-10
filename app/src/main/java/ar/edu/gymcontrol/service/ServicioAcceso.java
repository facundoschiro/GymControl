package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.exceptions.SocioNoEncontradoException;
import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.repository.SocioRepo;
import ar.edu.gymcontrol.service.validador.ValidadorSocio;

import java.util.List;

public class ServicioAcceso {
    private final SocioRepo socios;
    private final List<ValidadorSocio> validadores;

    public ServicioAcceso(SocioRepo socios, List<ValidadorSocio> validadores) {
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
