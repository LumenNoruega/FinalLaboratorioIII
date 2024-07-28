package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.PrestamoRequest;
import ar.edu.utn.frbb.tup.controller.PrestamosClienteResponse;
import ar.edu.utn.frbb.tup.controller.PrestamoResponse;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PrestamoServiceTest {

    // mocks que simulan el comportamiento de las clases dependientes
    @Mock
    private ClienteDao clienteDao;

    @Mock
    private PrestamoDao prestamoDao;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private ServicioCalificacionCredito servicioCalificacionCredito;

    // inyecta los mocks en la instancia de PrestamoService
    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // prueba para verificar que se lance una excepción si no se encuentra el cliente
    @Test
    public void testSolicitarPrestamo_ClienteNoEncontrado() {
        // Configura el mock para que devuelva null al buscar un cliente
        when(clienteDao.find(anyLong(), anyBoolean())).thenReturn(null);
        // Crea una solicitud de préstamo
        PrestamoRequest request = new PrestamoRequest();
        request.setNumeroCliente(1L);
        // Verifica que se lance una excepción al solicitar el préstamo
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.solicitarPrestamo(request);
        });
        // Verifica que el mensaje de la excepción sea el esperado
        assertEquals("Cliente no encontrado", exception.getMessage());
    }

    // Prueba para verificar que se lance una excepción si no se encuentra la cuenta del cliente en la
    // moneda solicitada
    @Test
    public void testSolicitarPrestamo_CuentaNoEncontrada() {
        Cliente cliente = new Cliente();
        // Configura el mock para que devuelva un cliente pero no una cuenta
        when(clienteDao.find(anyLong(), anyBoolean())).thenReturn(cliente);
        when(cuentaService.findCuentaByClienteAndMoneda(any(Cliente.class), anyString())).thenReturn(null);

        PrestamoRequest request = new PrestamoRequest();
        request.setNumeroCliente(1L);
        request.setMoneda("USD");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.solicitarPrestamo(request);
        });

        assertEquals("El cliente no tiene una cuenta en la moneda solicitada", exception.getMessage());
    }

    // Prueba para verificar que se lance una excepción si la calificación crediticia no es aprobada
    @Test
    public void testSolicitarPrestamo_CalificacionNoAprobada() {
        Cliente cliente = new Cliente();
        Cuenta cuenta = new Cuenta();
        when(clienteDao.find(anyLong(), anyBoolean())).thenReturn(cliente);
        when(cuentaService.findCuentaByClienteAndMoneda(any(Cliente.class), anyString())).thenReturn(cuenta);
        when(servicioCalificacionCredito.verificarCalificacion(anyString())).thenReturn(false);

        PrestamoRequest request = new PrestamoRequest();
        request.setNumeroCliente(1L);
        request.setMoneda("USD");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.solicitarPrestamo(request);
        });

        assertEquals("El cliente no tiene una buena calificación crediticia", exception.getMessage());
    }

    // Prueba para verificar el caso exitoso de solicitud de préstamo
    @Test
    public void testSolicitarPrestamo_Aprobado() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setBalance(1000.0);

        when(clienteDao.find(anyLong(), anyBoolean())).thenReturn(cliente);
        when(cuentaService.findCuentaByClienteAndMoneda(any(Cliente.class), anyString())).thenReturn(cuenta);
        when(servicioCalificacionCredito.verificarCalificacion(anyString())).thenReturn(true);
        when(prestamoDao.save(any(Prestamo.class))).thenReturn(new Prestamo());

        PrestamoRequest request = new PrestamoRequest();
        request.setNumeroCliente(1L);
        request.setMoneda("USD");
        request.setMontoPrestamo(1000.0);
        request.setPlazoMeses(12);

        // Llama al servicio para solicitar el préstamo
        PrestamoResponse response = prestamoService.solicitarPrestamo(request);
        // Verifica que la respuesta no sea nula y que tenga los valores esperados
        assertNotNull(response);
        assertEquals("APROBADO", response.getEstado());
        assertEquals("El monto del préstamo fue acreditado en su cuenta", response.getMensaje());
        // Verifica que se actualizó el saldo de la cuenta y que se guardó el préstamo
        verify(cuentaService, times(1)).actualizarSaldo(eq(123L), eq(2000.0));
        verify(prestamoDao, times(1)).save(any(Prestamo.class));
    }

    // Prueba para verificar que se lance una excepción si no se encuentra el préstamo al registrar el pago de una cuota
    @Test
    public void testRegistrarPagoCuota_PrestamoNoEncontrado() {
        when(prestamoDao.findById(anyLong())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPagoCuota(1L, 1);
        });

        assertEquals("Préstamo no encontrado", exception.getMessage());
    }

    // Prueba para verificar que se lance una excepción si el número de cuotas es inválido
    @Test
    public void testRegistrarPagoCuota_NumeroCuotasInvalido() {
        Prestamo prestamo = new Prestamo();
        when(prestamoDao.findById(anyLong())).thenReturn(prestamo);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            prestamoService.registrarPagoCuota(1L, 0);
        });

        assertEquals("El número de cuotas debe ser mayor que cero", exception.getMessage());
    }

    // Prueba para verificar el caso exitoso de registro de pago de cuota
    @Test
    public void testRegistrarPagoCuota_Exito() {
        Prestamo prestamo = new Prestamo();
        prestamo.setPlazoMeses(12);
        prestamo.setPagosRealizados(5);
        prestamo.setSaldoRestante(5000.0);

        // Configura un plan de pago
        Prestamo.PlanPago planPago = new Prestamo.PlanPago();
        planPago.setMonto(1000.0);
        List<Prestamo.PlanPago> planPagos = new ArrayList<>();
        planPagos.add(planPago);

        prestamo.setPlanPagos(planPagos);

        when(prestamoDao.findById(anyLong())).thenReturn(prestamo);
        // Llama al servicio para registrar el pago de la cuota
        prestamoService.registrarPagoCuota(1L, 2);
        // Verifica que se actualizaron los pagos realizados y el saldo restante
        assertEquals(7, prestamo.getPagosRealizados());
        assertEquals(3000.0, prestamo.getSaldoRestante());

        verify(prestamoDao, times(1)).save(prestamo);
    }

    // Prueba para verificar la obtención de préstamos por cliente
    @Test
    public void testObtenerPrestamosPorCliente() {
        Long clienteId = 1L;
        List<Prestamo> prestamos = new ArrayList<>();
        Prestamo prestamo = new Prestamo();
        prestamo.setMontoPrestamo(1000.0);
        prestamo.setPlazoMeses(12);
        prestamo.setPagosRealizados(5);
        prestamo.setSaldoRestante(500.0);
        prestamos.add(prestamo);

        when(prestamoDao.findByClienteId(clienteId)).thenReturn(prestamos);

        PrestamosClienteResponse response = prestamoService.obtenerPrestamosPorCliente(clienteId);

        // Verifica que la respuesta no sea nula y que tenga los valores esperados
        assertNotNull(response);
        assertEquals(clienteId, response.getNumeroCliente());
        assertEquals(1, response.getPrestamos().size());

        PrestamoDto prestamoDto = response.getPrestamos().get(0);
        assertEquals(1000.0, prestamoDto.getMonto());
        assertEquals(12, prestamoDto.getPlazoMeses());
        assertEquals(5, prestamoDto.getPagosRealizados());
        assertEquals(500.0, prestamoDto.getSaldoRestante());
    }
}
