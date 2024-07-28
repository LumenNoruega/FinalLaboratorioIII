package ar.edu.utn.frbb.tup.controller;

import java.util.List;

// DTO  para transferir información sobre el prestamo
// Aca estan los datos necesarios para procesar el préstamo, monto, plazo,los pagos realizados, y el saldo restante.
public class PrestamoDto {
    private double monto;
    private int plazoMeses;
    private int pagosRealizados;
    private double saldoRestante;

    // Getters y setters para acceder y modificar los atributos del DTO

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public int getPagosRealizados() {
        return pagosRealizados;
    }

    public void setPagosRealizados(int pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }

    public double getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(double saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    }
