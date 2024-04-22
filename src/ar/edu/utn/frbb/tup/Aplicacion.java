package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cuenta;
import ar.edu.utn.frbb.tup.utils.SimpleAdder;

import java.time.LocalDateTime;

class Aplicacion {

    public static void main(String args[]) {
        if (args.length != 2){
            System.out.println("No se proporcionaron args validos");
        } else {
            System.out.println("Hola " + args[0] + " " + args[1]);
        }

        SimpleAdder adder = new SimpleAdder();
        System.out.println("el resultado de 3+2 es " + adder.add(3,2));
        System.out.println("el resultado de 4+5 es " + adder.add(4,5));
        System.out.println("la suma historica es: " + adder.getSumaHistorica());

        SimpleAdder adder2 = new SimpleAdder();
        adder2.add(1,1);
        System.out.println("la suma historica es: " + adder2.getSumaHistorica());

        Cuenta cuentaLuciano = new Cuenta().setNombre("Luciano").setFechaCreacion(LocalDateTime.now()).setBalance(1835);
        Cuenta cuentaAndoni = new Cuenta().setNombre("Andoni").setFechaCreacion(LocalDateTime.now()).setBalance(2150);

        SimpleAdder cuentasAdder = new SimpleAdder();
        int totalCuentas = cuentasAdder.add(cuentaLuciano.getBalance(), cuentaAndoni.getBalance());

    }
}
