package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    CuentaDao cuentaDao = new CuentaDao();

    ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public void darDeAltaCliente(Cliente cliente) throws ClienteAlreadyExistsException {
/*
        if (clienteDao.find(cliente.getDni()) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }
*/

        if(cliente.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.save(cliente);
    }

    public void agregarCuenta(Cuenta cuenta, Cliente cliente) throws TipoCuentaAlreadyExistsException {
        if (cliente.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        cliente.addCuenta(cuenta);
        clienteDao.save(cliente);

    }

    public Cliente buscarClientePorDni(long dni) {
        Cliente cliente = clienteDao.find(dni);

        if(cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }

        return cliente;
    }
}
