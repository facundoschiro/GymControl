package ar.edu.gymcontrol.exceptions;
public class SocioNoEncontradoException extends RuntimeException {
    public SocioNoEncontradoException(String dni) { super("Socio no encontrado: " + dni); }
}
