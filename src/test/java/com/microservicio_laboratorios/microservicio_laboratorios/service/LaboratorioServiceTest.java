package com.microservicio_laboratorios.microservicio_laboratorios.service;

import com.microservicio_laboratorios.microservicio_laboratorios.exception.BadRequestException;
import com.microservicio_laboratorios.microservicio_laboratorios.exception.ResourceNotFoundException;
import com.microservicio_laboratorios.microservicio_laboratorios.model.Laboratorio;
import com.microservicio_laboratorios.microservicio_laboratorios.repository.LaboratorioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @InjectMocks
    private LaboratorioService laboratorioService;

    @Test
    void listarTodos_RetornarListaDeLaboratorios() {
        //Arrange
        Laboratorio lab1 = Laboratorio.builder().id(1L).nombre("Lab 1").build();
        Laboratorio lab2 = Laboratorio.builder().id(2L).nombre("Lab 2").build();
        when(laboratorioRepository.findAll()).thenReturn(Arrays.asList(lab1, lab2));

        //Act
        List<Laboratorio> resultado = laboratorioService.listarTodos();

        //Assert
        assertEquals(2, resultado.size());
        verify(laboratorioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorTipo_RetornarLaboratorios_CuandoExisten() {
        //Arrange
        String tipo = "Química";
        Laboratorio lab = Laboratorio.builder().tipoAnalisis(tipo).build();
        when(laboratorioRepository.findByTipoAnalisis(tipo)).thenReturn(Collections.singletonList(lab));

        //Act
        List<Laboratorio> resultado = laboratorioService.buscarPorTipo(tipo);

        //Assert
        assertFalse(resultado.isEmpty());
        assertEquals(tipo, resultado.get(0).getTipoAnalisis());
    }

    @Test
    void buscarPorTipo_LanzarExcepcion_CuandoNoExisten() {
        //Arrange
        String tipo = "Inexistente";
        when(laboratorioRepository.findByTipoAnalisis(tipo)).thenReturn(Collections.emptyList());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> laboratorioService.buscarPorTipo(tipo));
    }

    @Test
    void obtenerPorId_RetornarLaboratorio_CuandoExiste() {
        //Arrange
        Long id = 1L;
        Laboratorio lab = Laboratorio.builder().id(id).build();
        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(lab));

        //Act
        Laboratorio resultado = laboratorioService.obtenerPorId(id);

        //Assert
        assertEquals(id, resultado.getId());
    }

    @Test
    void obtenerPorId_LanzarExcepcion_CuandoNoExiste() {
        //Arrange
        Long id = 1L;
        when(laboratorioRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> laboratorioService.obtenerPorId(id));
    }

    @Test
    void crear_DeberiaGuardarLaboratorio_CuandoEsValido() {
        //Arrange
        Laboratorio lab = Laboratorio.builder().nombre("Nuevo Lab").build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(lab);

        //Act
        Laboratorio resultado = laboratorioService.crear(lab);

        //Assert
        assertNotNull(resultado);
        assertEquals("Nuevo Lab", resultado.getNombre());
    }

    @Test
    void crear_LanzarExcepcion_CuandoIdYaExiste() {
        //Arrange
        Long id = 1L;
        Laboratorio lab = Laboratorio.builder().id(id).build();
        when(laboratorioRepository.existsById(id)).thenReturn(true);

        //Act & Assert
        assertThrows(BadRequestException.class, () -> laboratorioService.crear(lab));
    }

    @Test
    void actualizar_ActualizarLaboratorio_CuandoExiste() {
        //Arrange
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder().id(id).nombre("Viejo Nombre").build();
        Laboratorio labNuevo = Laboratorio.builder().nombre("Nuevo Nombre").capacidad(50).estado("ACTIVO")
                .tipoAnalisis("Bio").build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        //Assert
        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals(50, resultado.getCapacidad());
    }

    @Test
    void eliminar_EliminarLaboratorio_CuandoExiste() {
        //Arrange
        Long id = 1L;
        Laboratorio lab = Laboratorio.builder().id(id).build();
        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(lab));

        //Act
        laboratorioService.eliminar(id);

        //Assert
        verify(laboratorioRepository, times(1)).delete(lab);
    }
}
