package ar.edu.utn.frbb.tup.controller;


import java.util.List;
//esta clase  sirve para estructurar la respuesta que se va a enviar al
// cliente despues de solicitar un préstamo.
public class PrestamoResponse {
    private String estado;
    private String mensaje;
    private List<PlanPago> planPagos;

    // Getters y setters
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<PlanPago> getPlanPagos() {
        return planPagos;
    }

    public void setPlanPagos(List<PlanPago> planPagos) {
        this.planPagos = planPagos;
    }

    // Clase interna para PlanPago
    public static class PlanPago {
        private int cuotaNro;
        private double monto;

        // Getters y setters
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