package ar.edu.utn.frbb.tup.controller;

// clase para registrar el pago de cuotas de un prestamo
// Contiene el número de cuotas que se quiere pagar.
public class CuotasPagoRequest {
    private int numeroCuotas;

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(int numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }
}
