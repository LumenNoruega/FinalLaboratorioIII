package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.List;

// clase que se utiliza para representar un préstamo
public class Prestamo {

    // Atributos del prestamo
    private Long id;
    private Cliente cliente;
    private double montoPrestamo;
    private String moneda;
    private int plazoMeses;
    private LocalDate fechaInicio;
    private List<PlanPago> planPagos;
    private double saldoRestante;
    private int pagosRealizados;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public List<PlanPago> getPlanPagos() {
        return planPagos;
    }

    public void setPlanPagos(List<PlanPago> planPagos) {
        this.planPagos = planPagos;
    }

    public double getSaldoRestante() {  // Nuevo getter agregado
        return saldoRestante;
    }

    public void setSaldoRestante(double saldoRestante) {  // Nuevo setter agregado
        this.saldoRestante = saldoRestante;
    }

    public int getPagosRealizados() {
        return pagosRealizados;
    }

    public void setPagosRealizados(int pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }

    // Clase interna para representar el plan de pago
    public static class PlanPago {
        private int cuotaNro;
        private double monto;

        // Getters y Setters
        public int getCuotaNro() {
            return cuotaNro;
        }

        public void setCuotaNro(int cuotaNro) {
            this.cuotaNro = cuotaNro;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(double monto) {
            this.monto = monto;
        }
    }
}
