package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.InvalidTipoPersonaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        Cliente clienteMenorDeEdad = new Cliente();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException, InvalidTipoPersonaException {
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        cliente.setDni(29857643);
        cliente.setNombre("Juan");
        cliente.setTipoPersona("fisica");

        when(clienteDao.find(cliente.getDni(), false)).thenReturn(null);

        clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.FISICA.toString());

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }

        @Test
        public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
            Cliente pepeRino = new Cliente();
            pepeRino.setDni(26456439);
            pepeRino.setNombre("Pepe");
            pepeRino.setApellido("Rino");
            pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
            pepeRino.setTipoPersona(TipoPersona.FISICA.toString());

            Cuenta cuenta = new Cuenta()
                    .setMoneda(TipoMoneda.PESOS)
                    .setBalance(500000)
                    .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

            when(clienteDao.find(26456439, false)).thenReturn(pepeRino);

            clienteService.agregarCuenta(cuenta, pepeRino.getDni());

            verify(clienteDao, times(1)).find(26456439, false);
            assertEquals(1, pepeRino.getCuentas().size());
            assertTrue(pepeRino.getCuentas().contains(cuenta));
            assertEquals(pepeRino, cuenta.getTitular());
        }
    //MÉTODOS SOLICITADOS PARA ClienteServiceTest.java
    //Agregar una CA$ y agregar otra cuenta con mismo tipo y moneda --> fallar (assertThrows)
    //Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
    //Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino
    //Testear clienteService.buscarPorDni
    
        @Test
        public void testAgregarMismaCuentaTipoMonedaFalla() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
            Cliente pepeRino = new Cliente();
            pepeRino.setDni(26456440);
            pepeRino.setNombre("Pepe");
            pepeRino.setApellido("Rino");
            pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
            pepeRino.setTipoPersona(TipoPersona.FISICA.toString());

            Cuenta cuenta1 = new Cuenta()
                    .setMoneda(TipoMoneda.PESOS)
                    .setBalance(500000)
                    .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

            Cuenta cuenta2 = new Cuenta()
                    .setMoneda(TipoMoneda.PESOS)
                    .setBalance(200000)
                    .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

            when(clienteDao.find(26456440, false)).thenReturn(pepeRino);

            clienteService.agregarCuenta(cuenta1, pepeRino.getDni());

            assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, pepeRino.getDni()));
        }
    @Test
    public void testAgregarCuentasDiferentesTiposMonedas() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456441);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(TipoPersona.FISICA.toString());

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(200000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        when(clienteDao.find(26456441, false)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta1, pepeRino.getDni());
        clienteService.agregarCuenta(cuenta2, pepeRino.getDni());

        assertEquals(2, pepeRino.getCuentas().size());
        assertTrue(pepeRino.getCuentas().contains(cuenta1));
        assertTrue(pepeRino.getCuentas().contains(cuenta2));
        assertEquals(pepeRino, cuenta1.getTitular());
        assertEquals(pepeRino, cuenta2.getTitular());
    }

    @Test
    public void testAgregarCuentasCajaAhorroPesosYCajaAhorroDolares() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456442);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(TipoPersona.FISICA.toString());

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(200000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456442, false)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta1, pepeRino.getDni());
        clienteService.agregarCuenta(cuenta2, pepeRino.getDni());

        assertEquals(2, pepeRino.getCuentas().size());
        assertTrue(pepeRino.getCuentas().contains(cuenta1));
        assertTrue(pepeRino.getCuentas().contains(cuenta2));
        assertEquals(pepeRino, cuenta1.getTitular());
        assertEquals(pepeRino, cuenta2.getTitular());
    }

    @Test
    public void testBuscarPorDniExito() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456443);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(TipoPersona.FISICA.toString());

        when(clienteDao.find(26456443, true)).thenReturn(pepeRino);

        Cliente foundCliente = clienteService.buscarClientePorDni(26456443);
        assertEquals(pepeRino, foundCliente);
    }

    @Test
    public void testBuscarPorDniFalla() {
        when(clienteDao.find(26456444, true)).thenReturn(null);

        Cliente foundCliente = clienteService.buscarClientePorDni(26456444);
        assertNull(foundCliente);
    }
}