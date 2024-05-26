package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;

public class MenuInputProcessor extends BaseInputProcessor{
    ClienteInputProcessor clienteInputProcessor = new ClienteInputProcessor();
    CuentaInputProcessor cuentaInputProcessor = new CuentaInputProcessor();
    ShowInfoCliente showInfoCliente = new ShowInfoCliente();
    ShowInfoCuenta showInfoCuenta = new ShowInfoCuenta();
    boolean exit = false;

    public void renderMenu(Banco banco) {

        while (!exit) {
            System.out.println("Bienveido a la aplicación de Banco!");
            System.out.println("1. Crear un nuevo Cliente");
            System.out.println("2. Crear una nueva Cuenta");
            System.out.println("3. Generar un movimiento");
            System.out.println("4. Mostrar información del Cliente.");
            System.out.println("5. Mostrar información de una Cuenta");
            System.out.println("6. Salir");
            System.out.print("Ingrese su opción (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    clienteInputProcessor.altaCliente();
                    break;
                case 2:
                    cuentaInputProcessor.altaCuenta();
                    break;
//            case 3:
//                performTransaction();
//                break;
                case 4:
                    System.out.println("Ingrese el dni del usuario que quiere mostrar: ");
                    long dni = Long.parseLong(scanner.nextLine());
                    showInfoCliente.mostrarInfoCliente(dni);
                    break;
                case 5:
                    System.out.println("Ingrese el id de la cuenta que quiere mostrar: ");
                    long id = Long.parseLong(scanner.nextLine());
                    showInfoCuenta.mostrarInfoCuenta(id);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor seleccione 1-4.");
            }
            clearScreen();
        }
    }
}
