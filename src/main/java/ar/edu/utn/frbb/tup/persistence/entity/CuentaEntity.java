package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.service.ClienteService;

import java.time.LocalDateTime;

public class CuentaEntity {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    private TipoMoneda moneda;
    private Cliente titular;

    // Constructor para convertir Cuenta a CuentaEntity
    public CuentaEntity(Cuenta cuenta) {
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta();
        this.moneda = cuenta.getMoneda();
        this.titular = cuenta.getTitular();
    }

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    // Método para convertir CuentaEntity a Cuenta
    public Cuenta toCuenta(ClienteService clienteService) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(this.numeroCuenta);
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setBalance(this.balance);
        cuenta.setTipoCuenta(this.tipoCuenta);
        cuenta.setMoneda(this.moneda);
        cuenta.setTitular(this.titular);
        return cuenta;
    }
}
