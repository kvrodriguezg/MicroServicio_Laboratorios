package com.microservicio_laboratorios.microservicio_laboratorios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicio_laboratorios.microservicio_laboratorios.model.Laboratorio;
import com.microservicio_laboratorios.microservicio_laboratorios.service.LaboratorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LaboratorioController.class)
class LaboratorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LaboratorioService laboratorioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Laboratorio laboratorio;

    @BeforeEach
    void setUp() {
        laboratorio = Laboratorio.builder()
                .id(1L)
                .nombre("Laboratorio Test")
                .capacidad(20)
                .estado("ACTIVO")
                .tipoAnalisis("Microbiología")
                .descripcion("Descripción de prueba")
                .ubicacion("Ubicación de prueba")
                .build();
    }

    @Test
    void listar_RetornarListaDeLaboratorios() throws Exception {
        when(laboratorioService.listarTodos()).thenReturn(Arrays.asList(laboratorio));

        mockMvc.perform(get("/api/laboratorios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laboratorio Test"));

        verify(laboratorioService, times(1)).listarTodos();
    }

    @Test
    void obtener_RetornarLaboratorio_CuandoExiste() throws Exception {
        when(laboratorioService.obtenerPorId(1L)).thenReturn(laboratorio);

        mockMvc.perform(get("/api/laboratorios/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laboratorio Test"));

        verify(laboratorioService, times(1)).obtenerPorId(1L);
    }

    @Test
    void buscarPorTipo_RetornarResultados_CuandoTipoEsProporcionado() throws Exception {
        when(laboratorioService.buscarPorTipo("Microbiología")).thenReturn(Collections.singletonList(laboratorio));

        mockMvc.perform(get("/api/laboratorios/buscar").param("tipo", "Microbiología"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(laboratorioService, times(1)).buscarPorTipo("Microbiología");
    }

    @Test
    void buscarPorTipo_RetornarTodos_CuandoTipoEsNulo() throws Exception {
        when(laboratorioService.listarTodos()).thenReturn(Arrays.asList(laboratorio));

        mockMvc.perform(get("/api/laboratorios/buscar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(laboratorioService, times(1)).listarTodos();
    }

    @Test
    void buscarPorTipo_RetornarTodos_CuandoTipoEsBlanco() throws Exception {
        when(laboratorioService.listarTodos()).thenReturn(Arrays.asList(laboratorio));

        mockMvc.perform(get("/api/laboratorios/buscar").param("tipo", "   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(laboratorioService, times(1)).listarTodos();
        verify(laboratorioService, never()).buscarPorTipo(any());
    }

    @Test
    void buscarPorTipo_RetornarTodos_CuandoTipoEsVacio() throws Exception {
        when(laboratorioService.listarTodos()).thenReturn(Arrays.asList(laboratorio));

        mockMvc.perform(get("/api/laboratorios/buscar").param("tipo", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(laboratorioService, times(1)).listarTodos();
        verify(laboratorioService, never()).buscarPorTipo(any());
    }

    @Test
    void crear_RetornarLaboratorioCreado_CuandoDatosSonValidos() throws Exception {
        when(laboratorioService.crear(any(Laboratorio.class))).thenReturn(laboratorio);

        mockMvc.perform(post("/api/laboratorios")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(laboratorio))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Laboratorio Test"));

        verify(laboratorioService, times(1)).crear(any(Laboratorio.class));
    }

    @Test
    void actualizar_RetornarLaboratorioActualizado() throws Exception {
        when(laboratorioService.actualizar(eq(1L), any(Laboratorio.class))).thenReturn(laboratorio);

        mockMvc.perform(put("/api/laboratorios/{id}", 1L)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(laboratorio))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laboratorio Test"));

        verify(laboratorioService, times(1)).actualizar(eq(1L), any(Laboratorio.class));
    }

    @Test
    void eliminar_RetornarNoContent() throws Exception {
        doNothing().when(laboratorioService).eliminar(1L);

        mockMvc.perform(delete("/api/laboratorios/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(laboratorioService, times(1)).eliminar(1L);
    }
}
