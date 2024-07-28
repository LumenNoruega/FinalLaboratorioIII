package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CuentaDao extends AbstractBaseDao {

    private final ClienteService clienteService;

    public CuentaDao(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getNumeroCuenta(), entity);
    }

    public void save(CuentaEntity cuentaEntity) {
        getInMemoryDatabase().put(cuentaEntity.getNumeroCuenta(), cuentaEntity);
    }

    public Cuenta find(long id) {
        CuentaEntity entity = (CuentaEntity) getInMemoryDatabase().get(id);
        return entity != null ? entity.toCuenta(clienteService) : null;
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object : getInMemoryDatabase().values()) {
            if (object instanceof CuentaEntity) {
                CuentaEntity cuenta = (CuentaEntity) object;
                if (cuenta.getTitular().getDni() == dni) {
                    cuentasDelCliente.add(cuenta.toCuenta(clienteService));
                }
            }
        }
        return cuentasDelCliente;
    }

    public CuentaEntity findById(long id) {
        return (CuentaEntity) getInMemoryDatabase().get(id);
    }

    public List<CuentaEntity> findAll() {
        List<CuentaEntity> todasLasCuentas = new ArrayList<>();
        for (Object object : getInMemoryDatabase().values()) {
            if (object instanceof CuentaEntity) {
                todasLasCuentas.add((CuentaEntity) object);
            }
        }
        System.out.println("Todas las cuentas: " + todasLasCuentas);
        return todasLasCuentas;
    }
}

