package ar.edu.utn.frbb.tup.controller;

import java.util.List;
// Esta clase se usa para representar la respuesta que contiene la información de los préstamos de un cliente.
// Incluye el número de cliente y una lista de préstamos asociados a ese cliente.
public class PrestamosClienteResponse {
    private Long numeroCliente;
    private List<PrestamoDto> prestamos;

    public Long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(Long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public List<PrestamoDto> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<PrestamoDto> prestamos) {
        this.prestamos = prestamos;
    }
}
