package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuInputProcessor {

    private final ClienteInputProcessor clienteInputProcessor;
    private final CuentaInputProcessor cuentaInputProcessor;
    private final ShowInfoCliente showInfoCliente;
    private final ShowInfoCuenta showInfoCuenta;
    private final Scanner scanner = new Scanner(System.in);
    private boolean exit = false;

    
    public MenuInputProcessor(ClienteInputProcessor clienteInputProcessor, CuentaInputProcessor cuentaInputProcessor,
        ShowInfoCliente showInfoCliente, ShowInfoCuenta showInfoCuenta) {
        this.clienteInputProcessor = clienteInputProcessor;
        this.cuentaInputProcessor = cuentaInputProcessor;
        this.showInfoCliente = showInfoCliente;
        this.showInfoCuenta = showInfoCuenta;
    }

    public void renderMenu() {
        while (!exit) {
            System.out.println("Bienvenido a la aplicación de Banco!");
            System.out.println("1. Crear un nuevo Cliente");

            System.out.println("2. Crear una nueva Cuenta");
            System.out.println("3. Generar un movimiento");
            System.out.println("4. Mostrar información del Cliente.");
            System.out.println("5. Mostrar información de una Cuenta");
            System.out.println("6. Salir");
            System.out.print("Ingrese su opción (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            try {
                switch (choice) {
                    case 1:
                        clienteInputProcessor.altaCliente();
                        break;
                    case 2:
                        cuentaInputProcessor.altaCuenta();
                        break;
                    // case 3:
                    //     performTransaction();
                    //     break;
                    case 4:
                        System.out.println("Ingrese el DNI del cliente que desea mostrar: ");
                        long dni = Long.parseLong(scanner.nextLine());
                        showInfoCliente.mostrarInfoCliente(dni);
                        break;
                    case 5:
                        System.out.println("Ingrese el ID de la cuenta que desea mostrar: ");
                        long id = Long.parseLong(scanner.nextLine());
                        showInfoCuenta.mostrarInfoCuenta(id);
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor seleccione 1-6.");
                }
            } catch (ClienteAlreadyExistsException e) {
                System.out.println("Error: " + e.getMessage());
            }
            clearScreen();
        }
    }

    private void clearScreen() {
        // Método para limpiar la pantalla
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
