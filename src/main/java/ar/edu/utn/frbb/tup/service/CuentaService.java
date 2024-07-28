package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotSupportedExcepcion;
import ar.edu.utn.frbb.tup.model.exception.FondosInsuficientesException;
import ar.edu.utn.frbb.tup.model.exception.MonedaInvalidaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
//maneja la creación de cuentas, la validación de tipos de cuenta soportados, la obtención de
// cuentas y la actualización de saldos
@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteService clienteService;

    // Método para dar de alta una nueva cuenta para un cliente existente
    public void darDeAltaCuenta(Cuenta cuenta, long dni) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        // Verifica si la cuenta ya existe
        Cuenta cuentaExistente = cuentaDao.find(cuenta.getNumeroCuenta());
        if (cuentaExistente != null) {
            throw new CuentaAlreadyExistsException("Cuenta ya existe con el número de cuenta: " + cuenta.getNumeroCuenta());
        }
        // Busca el cliente por DNI
        Cliente cliente = clienteService.buscarClientePorDni(dni);
        if (cliente == null) {
            throw new ClienteAlreadyExistsException("Cliente no encontrado con el DNI: " + dni);
        }
        // Verifica si el cliente ya tiene una cuenta del mismo tipo
        for (Cuenta c : cliente.getCuentas()) {
            if (c.getTipoCuenta().equals(cuenta.getTipoCuenta())) {
                throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de tipo: " + cuenta.getTipoCuenta());
            }
        }

        // Asigna la cuenta al cliente y la guarda
        cuenta.setTitular(cliente);
        cuentaDao.save(cuenta);
        clienteService.agregarCuenta(cuenta, dni);
    }

    // Método para validar si un tipo de cuenta es soportado por el sistema.
    public void validarCuentaSoportada(TipoCuenta tipoCuenta) throws CuentaNotSupportedExcepcion {
        if (!tipoDeCuentaSoportada(tipoCuenta.toString())) {
            throw new CuentaNotSupportedExcepcion("Tipo de cuenta no soportado: " + tipoCuenta);
        }
    }

    // Método para encontrar una cuenta por su ID.
    public Cuenta find(long id) {
        CuentaEntity cuentaEntity = cuentaDao.findById(id);
        if (cuentaEntity == null) {
            return null;
        }
        return cuentaEntity.toCuenta(clienteService);
    }

    // Método para verificar si un tipo de cuenta es soportado por el sistema
    public boolean tipoDeCuentaSoportada(String tipoCuenta) {
        if (tipoCuenta == null) {
            return false;
        }
        switch (tipoCuenta) {
            case "CA$":
            case "CC$":
            case "CAU$S":
                return true;
            default:
                return false;
        }
    }

    // Método para obtener todas las cuentas del sistema
    public List<Cuenta> obtenerTodasLasCuentas() {
        try {
            List<CuentaEntity> cuentasEntities = cuentaDao.findAll();
            return cuentasEntities.stream()
                    .map(entity -> entity.toCuenta(clienteService))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener todas las cuentas", e);
        }
    }

    // Método para encontrar una cuenta por cliente y moneda.
    public Cuenta findCuentaByClienteAndMoneda(Cliente cliente, String moneda) {
        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta.getMoneda().toString().equalsIgnoreCase(moneda)) {
                return cuenta;
            }
        }
        return null;
    }

    // Método para actualizar el saldo de una cuenta.
    public void actualizarSaldo(Long numeroCuenta, double nuevoSaldo) {
        CuentaEntity cuentaEntity = cuentaDao.findById(numeroCuenta);
        if (cuentaEntity != null) {
            cuentaEntity.setBalance(nuevoSaldo);
            cuentaDao.save(cuentaEntity);
        }
    }
}
