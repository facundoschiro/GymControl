package ar.edu.gymcontrol.service.validador;

import ar.edu.gymcontrol.exceptions.SocioInactivoException;
import ar.edu.gymcontrol.model.EstadoSocio;
import ar.edu.gymcontrol.model.Socio;

public class ValidadorEstado implements ValidadorSocio {
    @Override public void validar(Socio s) {
        if (s.getEstado() != EstadoSocio.ACTIVO) throw new SocioInactivoException();
    }
}
