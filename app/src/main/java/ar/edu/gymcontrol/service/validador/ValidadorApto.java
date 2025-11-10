package ar.edu.gymcontrol.service.validador;

import ar.edu.gymcontrol.exceptions.AptoVencidoException;
import ar.edu.gymcontrol.model.Socio;
import java.time.LocalDate;

public class ValidadorApto implements ValidadorSocio {
    @Override public void validar(Socio s) {
        if (s.getAptoHasta().isBefore(LocalDate.now())) throw new AptoVencidoException();
    }
}
