package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

import java.util.HashMap;
import java.util.Map;

public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();

    public void darDeAltaCuenta(Cuenta cuenta) throws CuentaAlreadyExistsException {
        if(cuentaDao.find(cuenta.getNumeroCuenta()) == null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
        cuentaDao.save(cuenta);
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}
