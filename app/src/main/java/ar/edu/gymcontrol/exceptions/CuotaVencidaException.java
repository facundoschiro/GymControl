package ar.edu.gymcontrol.exceptions;
public class CuotaVencidaException extends RuntimeException {
    public CuotaVencidaException() { super("Cuota no vigente"); }
}
