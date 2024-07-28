package ar.edu.utn.frbb.tup.persistence;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseDao {
    // Base de datos en memoria simulada con una estructura de Map
    protected static Map<String, Map<Long, Object>> poorMansDatabase = new HashMap<>();
    
    // Método abstracto para obtener el nombre de la entidad
    protected abstract String getEntityName();

    // Método para obtener la base de datos en memoria para una entidad específica
    protected Map<Long, Object> getInMemoryDatabase() {
        // Verifica si la base de datos para la entidad existe, si no, la crea
    poorMansDatabase.computeIfAbsent(getEntityName(), k -> new HashMap<>());
    return poorMansDatabase.get(getEntityName());
    }
    
}

