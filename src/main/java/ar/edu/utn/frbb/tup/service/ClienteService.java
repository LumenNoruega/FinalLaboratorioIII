package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoPersonaInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    @Lazy
    private ClienteDao clienteDao;

    @Autowired
    private ClienteValidator clienteValidator;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public ClienteService() {
    }

    public Cliente darDeAltaCliente(Cliente cliente) throws ClienteAlreadyExistsException, TipoPersonaInvalidoException{
        // Verifica si el cliente ya existe en la base de datos en memoria
        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }


        // Verifica que el cliente sea mayor de 18 años
        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        // Verifica que el tipo de persona sea válido
        if (!"fisica".equalsIgnoreCase(cliente.getTipoPersona()) && !"juridica".equalsIgnoreCase(cliente.getTipoPersona())) {
            throw new TipoPersonaInvalidoException("El tipo de persona debe ser 'fisica' o 'juridica'");
        }
        // Guarda el cliente en la base de datos en memoria
        clienteDao.save(cliente);
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta, long dni) throws ClienteAlreadyExistsException, TipoCuentaAlreadyExistsException {
        // Busca el cliente en la base de datos en memoria
        Cliente cliente = clienteDao.find(dni, false);
        if (cliente == null) {
            throw new ClienteAlreadyExistsException("Cliente no encontrado con DNI: " + dni);
        }

        // Verifica si el cliente ya tiene una cuenta de este tipo y moneda
        if (cliente.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de este tipo y moneda");
        }

        // Agrega la cuenta al cliente y guarda los cambios
        cliente.addCuenta(cuenta);
        clienteDao.save(cliente);
    }

    public Cliente buscarClientePorDni(long dni) {
        // Busca el cliente en la base de datos en memoria y devuelve el cliente encontrado o null
        return clienteDao.find(dni, true);
    }

    public List<Cliente> obtenerTodosClientes() {
        // Obtiene todos los clientes desde la base de datos en memoria
        return clienteDao.findAll();
    }
}


