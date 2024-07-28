package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Prestamo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

//Prestamodao es como un simulador de base de datos en memoria para manejar
// prestamos, ya que no tenemos una base de datos
@Component
public class PrestamoDao {
    // se usa map para simular una base de datos en memoria
    private Map<Long, Prestamo> database = new HashMap<>();

    // metodo para guardar un prestamo en la "base de datos"
    public Prestamo save(Prestamo prestamo) {
        Long id = prestamo.getCliente().getDni(); // se usa el DNI del cliente como ID
        prestamo.setId(id);
        database.put(id, prestamo);
        return prestamo;
    }

    //busca un prestamo por su ID
    public Prestamo findById(Long id) {
        return database.get(id);
    }
    // Devuelve el prestamo que corresponde al ID proporcionado

    // busca todos los prestamos de un cliente por su DNI
    public List<Prestamo> findByClienteId(Long clienteId) {
        // Filtra los prestamos para encontrar los que pertenecen al cliente con es dni
        return database.values().stream()
                .filter(prestamo -> prestamo.getCliente().getDni() == clienteId)
                .collect(Collectors.toList());
    }

    public Map<Long, Prestamo> findAll() {
        return database;
    }
    //Devuelve el mapa completo de prestamos
}
