package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaControllerTest {

    @InjectMocks
    private CuentaController cuentaController;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para el método crearCuenta cuando el cliente no es encontrado
    @Test
    public void testCrearCuentaClienteNoEncontrado() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTitularDni(12345678);

        when(clienteService.buscarClientePorDni(cuentaDto.getTitularDni())).thenReturn(null);
        // Llama al método crearCuenta del controlador y captura la respuesta
        ResponseEntity<String> response = cuentaController.crearCuenta(cuentaDto);
        // Verifica que la respuesta tenga un estado 400 y el mensaje "Cliente no encontrado"
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cliente no encontrado", response.getBody());
    }

    // Prueba para el método obtenerTodasLasCuentas cuando se encuentran cuentas
    @Test
    public void testObtenerTodasLasCuentasSuccess() {
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new Cuenta());

        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(cuentas);

        ResponseEntity<List<Cuenta>> response = cuentaController.obtenerTodasLasCuentas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentas, response.getBody());
    }

    // Prueba para el método obtenerTodasLasCuentas cuando no se encuentran cuentas
    @Test
    public void testObtenerTodasLasCuentasNoContent() {
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Cuenta>> response = cuentaController.obtenerTodasLasCuentas();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Prueba para el método obtenerTodasLasCuentas cuando ocurre un error interno del servidor
    @Test
    public void testObtenerTodasLasCuentasInternalServerError() {
        when(cuentaService.obtenerTodasLasCuentas()).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<List<Cuenta>> response = cuentaController.obtenerTodasLasCuentas();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
