package ar.edu.gymcontrol.service.validador;

import ar.edu.gymcontrol.exceptions.CuotaVencidaException;
import ar.edu.gymcontrol.model.Socio;
import java.time.LocalDate;

public class ValidadorCuota implements ValidadorSocio {
    @Override public void validar(Socio s) {
        if (s.getCuotaHasta().isBefore(LocalDate.now())) throw new CuotaVencidaException();
    }
}
