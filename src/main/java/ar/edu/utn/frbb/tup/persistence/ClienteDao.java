package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteDao extends AbstractBaseDao {

    @Autowired
    @Lazy
    private CuentaDao cuentaDao;


    public Cliente find(long dni, boolean loadComplete) {
        ClienteEntity clienteEntity = (ClienteEntity) getInMemoryDatabase().get(dni);
        if (clienteEntity == null) {
            return null;
        }

        Cliente cliente = clienteEntity.toCliente();

        if (loadComplete) {

            for (Cuenta cuenta : cuentaDao.getCuentasByCliente(dni)) {
                cliente.addCuenta(cuenta);
            }
        }

        return cliente;
    }


    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(cliente.getDni(), entity); // Usar DNI como clave
    }


    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        for (Object entity : getInMemoryDatabase().values()) {
            if (entity instanceof ClienteEntity) {
                Cliente cliente = ((ClienteEntity) entity).toCliente();
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}

