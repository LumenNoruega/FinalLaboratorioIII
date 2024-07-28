package ar.edu.utn.frbb.tup.controller;

// representa la solicitud del prestamo, sirve para capturar y estructurar la
// informacion que un cliente proporciona cuando solicita un prestamo
public class PrestamoRequest {
    private Long numeroCliente;
    private int plazoMeses;
    private double montoPrestamo;
    private String moneda;

    // Getters y setters
    public Long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(Long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
