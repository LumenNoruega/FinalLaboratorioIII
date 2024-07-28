package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;

// Simulacion de una verificacion de calificacion crediticia, ya que no poseo una
// base de datos externa a la cual consultar esta información
@Service
public class ServicioCalificacionCredito {

    public boolean verificarCalificacion(String dni) {
        // Simula que el 95% de los clientes tienen buena calificación.
        return Math.random() > 0.05;
    }
}