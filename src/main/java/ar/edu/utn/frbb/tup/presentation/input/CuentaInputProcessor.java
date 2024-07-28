package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Component
public class CuentaInputProcessor extends BaseInputProcessor {

    private final ClienteService clienteService;
    private final CuentaService cuentaService;
    private final Scanner scanner = new Scanner(System.in);

   
    public CuentaInputProcessor(ClienteService clienteService, CuentaService cuentaService) {
        this.clienteService = clienteService;
        this.cuentaService = cuentaService;
    }

    public void altaCuenta() throws ClienteAlreadyExistsException {
        Cuenta cuenta = new Cuenta();
        clearScreen();

        System.out.println("Ingrese el tipo de cuenta ((C) Corriente, (A) Ahorro)");
        String tipoCuentaStr = scanner.nextLine().toUpperCase();
        while (!tipoCuentaStr.equals("C") && !tipoCuentaStr.equals("A")) {
            System.out.println("Tipo de cuenta inválido. Ingrese C o A: ");
            tipoCuentaStr = scanner.nextLine().toUpperCase();
        }

        TipoCuenta tipoCuenta = tipoCuentaStr.equals("C") ? TipoCuenta.CUENTA_CORRIENTE : TipoCuenta.CAJA_AHORRO;
        cuenta.setTipoCuenta(tipoCuenta);

        cuenta.setBalance(0);

        System.out.println("Ingrese el dni del titular de la cuenta: ");
        long dniTitular = Long.parseLong(scanner.nextLine());

        System.out.println("Elija el tipo de moneda de la cuenta - (P) Pesos (D) Dólares: ");
        String tipoMoneda = scanner.nextLine().toUpperCase();
        while (!tipoMoneda.equals("P") && !tipoMoneda.equals("D")) {
            System.out.println("Tipo de moneda inválido. Ingrese (P) Pesos o (D) Dolares: ");
            tipoMoneda = scanner.nextLine().toUpperCase();
        }

        TipoMoneda moneda = tipoMoneda.equals("P") ? TipoMoneda.PESOS : TipoMoneda.DOLARES;
        cuenta.setMoneda(moneda);

        cuenta.setFechaCreacion(LocalDate.now().atStartOfDay());

        try {
            cuentaService.darDeAltaCuenta(cuenta, dniTitular);
            System.out.println("Cuenta creada con éxito");
        } catch (TipoCuentaAlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        } catch (CuentaAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Error al dar de alta la cuenta: " + e.getMessage());
        } finally {
            clearScreen();
        }
    }
}
