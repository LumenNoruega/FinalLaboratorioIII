package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;

public class ClienteDao extends AbstractBaseDao{

    public Cliente find(long dni) {
        if (getInMemoryDatabase().get(dni) == null)
            return null;
        return  ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
