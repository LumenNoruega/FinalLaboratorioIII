package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ShowInfoCliente {
    @Autowired
    private ClienteService clienteService;

    public ShowInfoCliente(ClienteService clienteService) {
        this.clienteService = clienteService;
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

        if(cliente.getCuentas().isEmpty()) {
            System.out.println("El cliente no tiene cuentas asociadas.");
        } else {
            System.out.println("Cuentas del Cliente: ");
            System.out.println(cliente.getCuentas());
            for (Cuenta cuenta : cliente.getCuentas()) {
                System.out.println("Número de cuenta: " + cuenta.toString());
            }
        }

    }

}
