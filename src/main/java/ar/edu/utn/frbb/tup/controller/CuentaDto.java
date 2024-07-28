package ar.edu.utn.frbb.tup.controller;

import java.time.LocalDate;

public class CuentaDto {
    private long numeroCuenta;
    private LocalDate fechaCreacion;
    private double balance;
    private String tipoCuenta;
    private String moneda;
    private long titularDni;

    // Constructor vacío
    public CuentaDto() {
    }

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public long getTitularDni() {
        return titularDni;
    }

    public void setTitularDni(long titularDni) {
        this.titularDni = titularDni;
    }
}
