package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.ClienteService;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShowInfoCliente {
    private ClienteService clienteService;

    public ShowInfoCliente() {
        this.clienteService = new ClienteService();
    }

    public void mostrarInfoCliente(long dni) {
        Cliente cliente = clienteService.buscarClientePorDni(dni);

        if (cliente == null) {
            System.out.println("Cliente no encontrado");
        }
        System.out.println("Información del Cliente: ");
        System.out.println("Dni: " + cliente.getDni());
        System.out.println("Nombre y Apellido: " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Tipo de persona: " + cliente.getTipoPersona());
        System.out.println("Banco: " + cliente.getBanco());
        System.out.println("Fecha de nacimiento: " + cliente.getFechaNacimiento());
        System.out.println("Edad: " + cliente.getEdad());

        if(cliente.getListaCuentas().isEmpty()) {
            System.out.println("El cliente no tiene cuentas asociadas.");
        } else {
            System.out.println("Cuentas del Cliente: ");
            System.out.println(cliente.getListaCuentas());
            for (long cuentaId : cliente.getListaCuentas()) {
                System.out.println("Número de cuenta: " + cuentaId);
            }
        }

    }

}
