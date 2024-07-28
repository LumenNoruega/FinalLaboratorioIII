package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteEntity extends BaseEntity {

    private final String tipoPersona;
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaAlta;
    private String banco;
    private final LocalDate fechaNacimiento;
    private List<Long> cuentas;

    public ClienteEntity(Cliente cliente) {
        super(cliente.getDni());
        this.tipoPersona = cliente.getTipoPersona() ;
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.fechaAlta = cliente.getFechaAlta();
        this.banco = cliente.getBanco();
        this.fechaNacimiento = cliente.getFechaNacimiento();
        this.cuentas = new ArrayList<>();
        if (cliente.getCuentas() != null && !cliente.getCuentas().isEmpty()) {
            for (Cuenta c : cliente.getCuentas()) {
                cuentas.add(c.getNumeroCuenta());
            }
        }
    }

    public Cliente toCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(this.getId());
        cliente.setNombre(this.nombre);
        cliente.setApellido(this.apellido);
        cliente.setTipoPersona(this.tipoPersona); 
        cliente.setFechaAlta(this.fechaAlta);
        cliente.setBanco(this.banco);
        cliente.setFechaNacimiento(this.fechaNacimiento);

        return cliente;
    }
}
