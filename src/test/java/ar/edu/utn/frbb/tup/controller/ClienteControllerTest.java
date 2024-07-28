package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerTest {

    private MockMvc mockMvc;// Objeto para simular las peticiones HTTP

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    // Prueba para el método crearCliente cuando la creación es exitosa
    @Test
    void testCrearCliente_Exitoso() throws Exception {
        // Crea un ClienteDto con datos de prueba
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setFechaNacimiento(LocalDate.of(1985, 5, 15));
        clienteDto.setTipoPersona("fisica");
        clienteDto.setBanco("Banco Nación");

        // Crea un objeto Cliente con los mismos datos
        Cliente cliente = new Cliente();
        cliente.setDni(clienteDto.getDni());
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setFechaNacimiento(clienteDto.getFechaNacimiento());
        cliente.setTipoPersona(clienteDto.getTipoPersona());
        cliente.setBanco(clienteDto.getBanco());
        cliente.setFechaAlta(LocalDate.now());
        // Configura el mock para que cuando se llame a darDeAltaCliente, devuelva el objeto Cliente
        when(clienteService.darDeAltaCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/cliente")
                        .contentType("application/json")
                        .content("{ \"dni\": 12345678, \"nombre\": \"Juan\", \"apellido\": \"Pérez\", \"fechaNacimiento\": \"1985-05-15\", \"tipoPersona\": \"fisica\", \"banco\": \"Banco Nación\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(12345678)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.tipoPersona", is("fisica")))
                .andExpect(jsonPath("$.banco", is("Banco Nación")));
    }

    // Prueba para el método crearCliente cuando el tipo de persona es inválido
    @Test
    void testCrearCliente_TipoPersonaInvalido() throws Exception {
        mockMvc.perform(post("/cliente")
                        .contentType("application/json")
                        .content("{ \"dni\": 12345678, \"nombre\": \"Juan\", \"apellido\": \"Pérez\", \"fechaNacimiento\": \"1985-05-15\", \"tipoPersona\": \"invalid\", \"banco\": \"Banco Nación\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(1002)))
                .andExpect(jsonPath("$.errorMessage", is("El tipo de persona debe ser 'fisica' o 'juridica'")));
    }

    // Prueba para el método obtenerClientePorDni cuando la búsqueda es exitosa
    @Test
    void testObtenerClientePorDni_Exitoso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");

        when(clienteService.buscarClientePorDni(anyLong())).thenReturn(cliente);

        mockMvc.perform(get("/cliente/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(12345678)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")));
    }

    // Prueba para el método obtenerClientePorDni cuando el cliente no es encontrado
    @Test
    void testObtenerClientePorDni_NoEncontrado() throws Exception {
        when(clienteService.buscarClientePorDni(anyLong())).thenReturn(null);

        mockMvc.perform(get("/cliente/12345678"))
                .andExpect(status().isNotFound());
    }

    // Prueba para el método obtenerTodosClientes cuando hay clientes en el sistema
    @Test
    void testObtenerTodosClientes() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");

        when(clienteService.obtenerTodosClientes()).thenReturn(Collections.singletonList(cliente));
        // Simula una petición GET a /cliente y verifica la respuesta
        mockMvc.perform(get("/cliente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni", is(12345678)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[0].apellido", is("Pérez")));
    }

    // Prueba para el manejo de la excepción ClienteAlreadyExistsException
    @Test
    void testHandleClienteAlreadyExistsException() throws Exception {
        // Configura el mock para que cuando se llame a darDeAltaCliente, lance una ClienteAlreadyExistsException
        when(clienteService.darDeAltaCliente(any(Cliente.class)))
                .thenThrow(new ClienteAlreadyExistsException("Cliente ya existe"));

        mockMvc.perform(post("/cliente")
                        .contentType("application/json")
                        .content("{ \"dni\": 12345678, \"nombre\": \"Juan\", \"apellido\": \"Pérez\", \"fechaNacimiento\": \"1985-05-15\", \"tipoPersona\": \"fisica\", \"banco\": \"Banco Nación\" }"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode", is(1001)))
                .andExpect(jsonPath("$.errorMessage", is("Cliente ya existe")));
    }
}
