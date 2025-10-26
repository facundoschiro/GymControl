package ar.edu.gymcontrol.model;

public class Serie {
    private final int nro;
    private final Ejercicio ejercicio;
    private final int repeticiones;
    private final double pesoKg;

    public Serie(int nro, Ejercicio ejercicio, int repeticiones, double pesoKg) {
        if (repeticiones <= 0) throw new IllegalArgumentException("Reps > 0");
        if (pesoKg < 0) throw new IllegalArgumentException("Peso >= 0");
        this.nro = nro;
        this.ejercicio = ejercicio;
        this.repeticiones = repeticiones;
        this.pesoKg = pesoKg;
    }
    public int getNro() { return nro; }
    public Ejercicio getEjercicio() { return ejercicio; }
    public int getRepeticiones() { return repeticiones; }
    public double getPesoKg() { return pesoKg; }
    public double volumen() { return repeticiones * pesoKg; }
}
