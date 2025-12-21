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
        Laboratorio lab1 = Laboratorio.builder().id(1L).nombre("Lab 1").build();
        Laboratorio lab2 = Laboratorio.builder().id(2L).nombre("Lab 2").build();
        when(laboratorioRepository.findAll()).thenReturn(Arrays.asList(lab1, lab2));

        List<Laboratorio> resultado = laboratorioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(laboratorioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorTipo_RetornarLaboratorios_CuandoExisten() {
        String tipo = "Química";
        Laboratorio lab = Laboratorio.builder().tipoAnalisis(tipo).build();
        when(laboratorioRepository.findByTipoAnalisis(tipo)).thenReturn(Collections.singletonList(lab));

        List<Laboratorio> resultado = laboratorioService.buscarPorTipo(tipo);

        assertFalse(resultado.isEmpty());
        assertEquals(tipo, resultado.get(0).getTipoAnalisis());
    }

    @Test
    void buscarPorTipo_LanzarExcepcion_CuandoNoExisten() {
        String tipo = "Inexistente";
        when(laboratorioRepository.findByTipoAnalisis(tipo)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> laboratorioService.buscarPorTipo(tipo));
    }

    @Test
    void obtenerPorId_RetornarLaboratorio_CuandoExiste() {
        Long id = 1L;
        Laboratorio lab = Laboratorio.builder().id(id).build();
        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(lab));

        Laboratorio resultado = laboratorioService.obtenerPorId(id);

        assertEquals(id, resultado.getId());
    }

    @Test
    void obtenerPorId_LanzarExcepcion_CuandoNoExiste() {
        Long id = 1L;
        when(laboratorioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> laboratorioService.obtenerPorId(id));
    }

    @Test
    void crear_DeberiaGuardarLaboratorio_CuandoEsValido() {
        Laboratorio lab = Laboratorio.builder().nombre("Nuevo Lab").build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(lab);

        Laboratorio resultado = laboratorioService.crear(lab);

        assertNotNull(resultado);
        assertEquals("Nuevo Lab", resultado.getNombre());
    }

    @Test
    void crear_LanzarExcepcion_CuandoIdYaExiste() {
        Long id = 1L;
        Laboratorio lab = Laboratorio.builder().id(id).build();
        when(laboratorioRepository.existsById(id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> laboratorioService.crear(lab));
    }

    @Test
    void actualizar_ActualizarLaboratorio_CuandoExiste() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder().id(id).nombre("Viejo Nombre").build();
        Laboratorio labNuevo = Laboratorio.builder().nombre("Nuevo Nombre").capacidad(50).estado("ACTIVO")
                .tipoAnalisis("Bio").build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals(50, resultado.getCapacidad());
    }

    @Test
    void eliminar_EliminarLaboratorio_CuandoExiste() {
        Long id = 1L;
        Laboratorio lab = Laboratorio.builder().id(id).build();
        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(lab));

        laboratorioService.eliminar(id);

        verify(laboratorioRepository, times(1)).delete(lab);
    }

    @Test
    void eliminar_LanzarExcepcion_CuandoNoExiste() {
        Long id = 1L;
        when(laboratorioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> laboratorioService.eliminar(id));
        verify(laboratorioRepository, never()).delete(any());
    }

    @Test
    void actualizar_LanzarExcepcion_CuandoNoExiste() {
        Long id = 1L;
        Laboratorio labNuevo = Laboratorio.builder().nombre("Nuevo Nombre").build();
        when(laboratorioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> laboratorioService.actualizar(id, labNuevo));
        verify(laboratorioRepository, never()).save(any());
    }

    
    @Test
    void crear_AsignarImagenClinico_CuandoTipoEsClinico() {
        Laboratorio lab = Laboratorio.builder()
                .nombre("Lab Clínico")
                .tipoAnalisis("Clinico")
                .build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.crear(lab);

        assertEquals("assets/img/lab_clinico.png", resultado.getImagen());
    }

    @Test
    void crear_AsignarImagenInvestigacion_CuandoTipoEsInvestigacion() {
        Laboratorio lab = Laboratorio.builder()
                .nombre("Lab Investigación")
                .tipoAnalisis("Investigacion")
                .build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.crear(lab);

        assertEquals("assets/img/lab_investigacion.png", resultado.getImagen());
    }

    @Test
    void crear_AsignarImagenEducativo_CuandoTipoEsEducativo() {
        Laboratorio lab = Laboratorio.builder()
                .nombre("Lab Educativo")
                .tipoAnalisis("Educativo")
                .build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.crear(lab);

        assertEquals("assets/img/lab_educativo.png", resultado.getImagen());
    }

    @Test
    void crear_AsignarImagenIndustrial_CuandoTipoEsIndustrial() {
        Laboratorio lab = Laboratorio.builder()
                .nombre("Lab Industrial")
                .tipoAnalisis("Industrial")
                .build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.crear(lab);

        assertEquals("assets/img/lab_industrial.png", resultado.getImagen());
    }

    @Test
    void crear_AsignarImagenDefault_CuandoTipoEsDesconocido() {
        Laboratorio lab = Laboratorio.builder()
                .nombre("Lab Desconocido")
                .tipoAnalisis("TipoNoExistente")
                .build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.crear(lab);

        assertEquals("assets/img/lab_clinico.png", resultado.getImagen());
    }

    @Test
    void crear_AsignarImagenDefault_CuandoTipoEsNull() {
        Laboratorio lab = Laboratorio.builder()
                .nombre("Lab Sin Tipo")
                .tipoAnalisis(null)
                .build();
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.crear(lab);

        assertEquals("assets/img/lab_clinico.png", resultado.getImagen());
    }

    @Test
    void actualizar_AsignarImagenClinico_CuandoTipoEsClinico() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder()
                .id(id)
                .nombre("Lab Existente")
                .tipoAnalisis("Otro")
                .build();
        Laboratorio labNuevo = Laboratorio.builder()
                .nombre("Lab Actualizado")
                .tipoAnalisis("clinico")
                .capacidad(30)
                .estado("ACTIVO")
                .build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("assets/img/lab_clinico.png", resultado.getImagen());
    }

    @Test
    void actualizar_AsignarImagenInvestigacion_CuandoTipoEsInvestigacion() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder()
                .id(id)
                .nombre("Lab Existente")
                .tipoAnalisis("Otro")
                .build();
        Laboratorio labNuevo = Laboratorio.builder()
                .nombre("Lab Actualizado")
                .tipoAnalisis("investigacion")
                .capacidad(30)
                .estado("ACTIVO")
                .build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("assets/img/lab_investigacion.png", resultado.getImagen());
    }

    @Test
    void actualizar_AsignarImagenEducativo_CuandoTipoEsEducativo() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder()
                .id(id)
                .nombre("Lab Existente")
                .tipoAnalisis("Otro")
                .build();
        Laboratorio labNuevo = Laboratorio.builder()
                .nombre("Lab Actualizado")
                .tipoAnalisis("educativo")
                .capacidad(30)
                .estado("ACTIVO")
                .build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("assets/img/lab_educativo.png", resultado.getImagen());
    }

    @Test
    void actualizar_AsignarImagenIndustrial_CuandoTipoEsIndustrial() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder()
                .id(id)
                .nombre("Lab Existente")
                .tipoAnalisis("Otro")
                .build();
        Laboratorio labNuevo = Laboratorio.builder()
                .nombre("Lab Actualizado")
                .tipoAnalisis("industrial")
                .capacidad(30)
                .estado("ACTIVO")
                .build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("assets/img/lab_industrial.png", resultado.getImagen());
    }

    @Test
    void actualizar_AsignarImagenDefault_CuandoTipoEsDesconocido() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder()
                .id(id)
                .nombre("Lab Existente")
                .build();
        Laboratorio labNuevo = Laboratorio.builder()
                .nombre("Lab Actualizado")
                .tipoAnalisis("TipoDesconocido")
                .capacidad(30)
                .estado("ACTIVO")
                .build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("assets/img/lab_clinico.png", resultado.getImagen());
    }

    @Test
    void actualizar_AsignarImagenDefault_CuandoTipoEsNull() {
        Long id = 1L;
        Laboratorio labExistente = Laboratorio.builder()
                .id(id)
                .nombre("Lab Existente")
                .build();
        Laboratorio labNuevo = Laboratorio.builder()
                .nombre("Lab Actualizado")
                .tipoAnalisis(null)
                .capacidad(30)
                .estado("ACTIVO")
                .build();

        when(laboratorioRepository.findById(id)).thenReturn(Optional.of(labExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.actualizar(id, labNuevo);

        assertEquals("assets/img/lab_clinico.png", resultado.getImagen());
    }
}
