package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cliente;
import ar.edu.utn.frbb.tup.utils.Cuenta;
import ar.edu.utn.frbb.tup.utils.SimpleAdder;
import ar.edu.utn.frbb.tup.utils.TipoPersona;

import java.time.LocalDate;
import java.time.LocalDateTime;

class Aplicacion {

    public static void main(String args[]) {

        Cuenta cuentaLuciano = new Cuenta().setNombre("1").setFechaCreacion(LocalDateTime.now()).setBalance(1835);
        Cuenta cuentaLuciano2 = new Cuenta().setNombre("2").setFechaCreacion(LocalDateTime.now()).setBalance(1500);

        Cliente cliente = new Cliente();
        cliente.setNombre("Luciano");
        cliente.setApellido("Salotto");
        cliente.setFechaNacimiento(LocalDate.of(1978, 2, 7));
        cliente.setBanco("Provincia");
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setFechaAlta(LocalDate.now());
        cliente.addCuenta(cuentaLuciano);
        cliente.addCuenta(cuentaLuciano2);

        System.out.println("Luciano");

    }
}
