package ar.edu.utn.frbb.tup.controller;


import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ClienteService clienteService;

    // POST que se encarga de crear una nueva cuenta bancaria
    // Primero, busca al cliente por DNI. Si no se encuentra el cliente, devuelve un error
    // Si el cliente existe, crea una nueva cuenta con los datos proporcionados en el DTO.
    // Luego, intenta dar de alta la cuenta en el servicio. Si ocurre algún error (como una cuenta que ya existe o tipo de cuenta no válido), devuelve un error
    @PostMapping("/crearCuenta")
    public ResponseEntity<String> crearCuenta(@RequestBody CuentaDto cuentaDto) {
        try {
            Cliente cliente = clienteService.buscarClientePorDni(cuentaDto.getTitularDni());
            if (cliente == null) {
                return ResponseEntity.badRequest().body("Cliente no encontrado");
            }

            Cuenta cuenta = new Cuenta();
            cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta());
            cuenta.setFechaCreacion(cuentaDto.getFechaCreacion().atStartOfDay());
            cuenta.setBalance(cuentaDto.getBalance());
            cuenta.setTipoCuenta(TipoCuenta.valueOf(cuentaDto.getTipoCuenta()));
            cuenta.setMoneda(TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase()));
            cuenta.setTitular(cliente);

            cuentaService.darDeAltaCuenta(cuenta, cliente.getDni());
            return ResponseEntity.ok("Cuenta creada exitosamente.");
        } catch (CuentaAlreadyExistsException | TipoCuentaAlreadyExistsException | ClienteAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error en los datos proporcionados: " + e.getMessage());
        }
    }


    // GET que obtiene todas las cuentas bancarias existentes.
    // Llama al servicio para pedir la lista de cuentas.
    // Si no hay cuentas, devuelve un error (No Content).
    // Si se produce un error en la recuperación, devuelve error: INTERNAL_SERVER_ERROR
    @GetMapping("/cuentas")
    public ResponseEntity<List<Cuenta>> obtenerTodasLasCuentas() {
        try {
            List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
            if (cuentas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


