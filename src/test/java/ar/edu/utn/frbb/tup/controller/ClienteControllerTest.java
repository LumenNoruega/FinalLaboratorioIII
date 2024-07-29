package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.InvalidTipoPersonaException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClienteValidator clienteValidator;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteDto clienteDto;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteDto = new ClienteDto();
        clienteDto.setDni(12345678);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setFechaNacimiento(LocalDate.of(1980, 1, 1));
        clienteDto.setTipoPersona("fisica");
        clienteDto.setBanco("BancoNacional");

        cliente = new Cliente();
        cliente.setDni(clienteDto.getDni());
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setFechaNacimiento(clienteDto.getFechaNacimiento());
        cliente.setTipoPersona(clienteDto.getTipoPersona());
        cliente.setBanco(clienteDto.getBanco());
        cliente.setFechaAlta(LocalDate.now());
    }

    @Test
    void testCrearCliente() throws Exception {
        when(clienteService.darDeAltaCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(12345678)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.tipoPersona", is("fisica")))
                .andExpect(jsonPath("$.banco", is("BancoNacional")));
    }

    @Test
    void testCrearClienteAlreadyExists() throws Exception {
        doThrow(new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + clienteDto.getDni()))
                .when(clienteService).darDeAltaCliente(any(Cliente.class));

        mockMvc.perform(post("/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode", is(1001)))
                .andExpect(jsonPath("$.errorMessage", is("Ya existe un cliente con DNI " + clienteDto.getDni())));
    }

    @Test
    void testObtenerClientePorDni() throws Exception {
        when(clienteService.buscarClientePorDni(12345678)).thenReturn(cliente);

        mockMvc.perform(get("/cliente/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(12345678)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.tipoPersona", is("fisica")))
                .andExpect(jsonPath("$.banco", is("BancoNacional")));
    }

    @Test
    void testObtenerClientePorDniNotFound() throws Exception {
        when(clienteService.buscarClientePorDni(12345678)).thenReturn(null);

        mockMvc.perform(get("/cliente/12345678"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testObtenerTodosClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.obtenerTodosClientes()).thenReturn(clientes);

        mockMvc.perform(get("/cliente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dni", is(12345678)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[0].apellido", is("Pérez")))
                .andExpect(jsonPath("$[0].tipoPersona", is("fisica")))
                .andExpect(jsonPath("$[0].banco", is("BancoNacional")));
    }

    @Test
    void testInvalidTipoPersonaException() throws Exception {
        clienteDto.setTipoPersona("invalido");

        doThrow(new InvalidTipoPersonaException("El tipo de persona debe ser 'fisica' o 'juridica'"))
                .when(clienteValidator).validate(any(ClienteDto.class));

        mockMvc.perform(post("/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is(1002)))
                .andExpect(jsonPath("$.errorMessage", is("El tipo de persona debe ser 'fisica' o 'juridica'")));
    }
}
