package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoControllerTest {

    // Mock para simular el comportamiento de PrestamoService
    @Mock
    private PrestamoService prestamoService;

    // Injecta los mocks en la instancia de PrestamoController
    @InjectMocks
    private PrestamoController prestamoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para el metodo solicitarPrestamo del controlador
    @Test
    void testSolicitarPrestamo() {

        PrestamoRequest request = new PrestamoRequest();
        PrestamoResponse response = new PrestamoResponse();
        // Configura el mock para devolver la respuesta
        when(prestamoService.solicitarPrestamo(request)).thenReturn(response);

        ResponseEntity<PrestamoResponse> result = prestamoController.solicitarPrestamo(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    // Prueba para obtener préstamos por ID de cliente cuando se encuentra con éxito
    @Test
    void testGetPrestamosByClienteId_Success() {

        Long clienteId = 1L;
        PrestamosClienteResponse response = new PrestamosClienteResponse();
        response.setPrestamos(List.of(new PrestamoDto())); // Asegúrate de que PrestamoDto está correctamente definido
        when(prestamoService.obtenerPrestamosPorCliente(clienteId)).thenReturn(response);

        ResponseEntity<PrestamosClienteResponse> result = prestamoController.getPrestamosByClienteId(clienteId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    // Prueba para obtener préstamos por ID de cliente cuando no se encuentran
    @Test
    void testGetPrestamosByClienteId_NotFound() {

        Long clienteId = 1L;
        PrestamosClienteResponse response = new PrestamosClienteResponse();
        response.setPrestamos(Collections.emptyList());
        when(prestamoService.obtenerPrestamosPorCliente(clienteId)).thenReturn(response);


        ResponseEntity<PrestamosClienteResponse> result = prestamoController.getPrestamosByClienteId(clienteId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    // Prueba para obtener prestamos por ID de cliente cuando ocurre una excepción
    @Test
    void testGetPrestamosByClienteId_Exception() {

        Long clienteId = 1L;
        when(prestamoService.obtenerPrestamosPorCliente(clienteId)).thenThrow(new RuntimeException());

        ResponseEntity<PrestamosClienteResponse> result = prestamoController.getPrestamosByClienteId(clienteId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    // Prueba para registrar el pago de cuotas con éxito
    @Test
    void testPagarCuotas_Success() {

        Long prestamoId = 1L;
        CuotasPagoRequest request = new CuotasPagoRequest();
        request.setNumeroCuotas(2);
        doNothing().when(prestamoService).registrarPagoCuota(prestamoId, request.getNumeroCuotas());

        ResponseEntity<String> result = prestamoController.pagarCuotas(prestamoId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());// Verifica que el estado HTTP sea 200 OK
        assertEquals("Pago de cuotas registrado exitosamente", result.getBody());
    }

    // Prueba para manejar un caso de solicitud de pago de cuotas con error
    @Test
    void testPagarCuotas_BadRequest() {
        Long prestamoId = 1L;// ID del préstamo
        CuotasPagoRequest request = new CuotasPagoRequest();// Crea una solicitud de pago de cuotas
        request.setNumeroCuotas(2);// Establece el número de cuotas
        doThrow(new IllegalArgumentException("Error al registrar el pago")).when(prestamoService).registrarPagoCuota(prestamoId, request.getNumeroCuotas());

        ResponseEntity<String> result = prestamoController.pagarCuotas(prestamoId, request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Error al registrar el pago", result.getBody());
    }
}
