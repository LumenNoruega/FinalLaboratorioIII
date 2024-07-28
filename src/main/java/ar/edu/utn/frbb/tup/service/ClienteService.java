package ar.edu.utn.frbb.tup.service;

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

        // Verifica que el DNI tenga 8 dígitos
        String dniString = String.valueOf(cliente.getDni());
        if (dniString.length() != 8) {
            throw new IllegalArgumentException("El DNI debe tener 8 números");
        }

        // Verifica que la fecha de nacimiento no sea nula
        if (cliente.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        // Verifica que el cliente sea mayor de 18 años
        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        // Verifica que el nombre, apellido y banco contengan solo letras y caracteres especiales comunes en nombres
        if (cliente.getNombre() == null || !cliente.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+")) {
            throw new IllegalArgumentException("El nombre debe contener solo letras");
        }
        if (cliente.getApellido() == null || !cliente.getApellido().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+")) {
            throw new IllegalArgumentException("El apellido debe contener solo letras");
        }
        if (cliente.getBanco() == null || !cliente.getBanco().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+")) {
            throw new IllegalArgumentException("El banco debe contener solo letras");
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


