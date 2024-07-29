package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import org.springframework.stereotype.Component;

@Component
public class ClienteValidator {

    public void validate(ClienteDto clienteDto) {
        if (!"fisica".equalsIgnoreCase(clienteDto.getTipoPersona()) &&
                !"juridica".equalsIgnoreCase(clienteDto.getTipoPersona())) {
            throw new IllegalArgumentException("El tipo de persona debe ser 'fisica' o 'juridica'");
        }

        if (clienteDto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        // Verifica que el DNI tenga 8 dígitos
        String dniString = String.valueOf(clienteDto.getDni());
        if (dniString.length() != 8) {
            throw new IllegalArgumentException("El DNI debe tener 8 números");
        }

        // Verifica que el nombre, apellido y banco contengan solo letras y caracteres especiales comunes en nombres
        if (clienteDto.getNombre() == null || !clienteDto.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+")) {
            throw new IllegalArgumentException("El nombre debe contener solo letras");
        }
        if (clienteDto.getApellido() == null || !clienteDto.getApellido().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+")) {
            throw new IllegalArgumentException("El apellido debe contener solo letras");
        }
        if (clienteDto.getBanco() == null || !clienteDto.getBanco().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+")) {
            throw new IllegalArgumentException("El banco debe contener solo letras");
        }
    }
}


