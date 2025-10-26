package ar.edu.gymcontrol.service;

import ar.edu.gymcontrol.exceptions.*;
import ar.edu.gymcontrol.model.Socio;
import ar.edu.gymcontrol.repository.SocioRepo;

import java.time.LocalDate;

public class ServicioAcceso {
    private final SocioRepo socios;

    public ServicioAcceso(SocioRepo socios) { this.socios = socios; }

    public ValidacionAcceso validarPorDni(String dni) {
        try {
            Socio s = socios.findByDni(dni).orElseThrow(() -> new SocioNoEncontradoException(dni));
            if (s.getEstado() != ar.edu.gymcontrol.model.EstadoSocio.ACTIVO) throw new SocioInactivoException();
            if (s.getAptoHasta().isBefore(LocalDate.now())) throw new AptoVencidoException();
            if (s.getCuotaHasta().isBefore(LocalDate.now())) throw new CuotaVencidaException();
            return new ValidacionAcceso(true, "Acceso permitido a " + s);
        } catch (RuntimeException ex) {
            return new ValidacionAcceso(false, ex.getMessage());
        }
    }
}
