package com.microservicio_laboratorios.microservicio_laboratorios.service;

import com.microservicio_laboratorios.microservicio_laboratorios.exception.BadRequestException;
import com.microservicio_laboratorios.microservicio_laboratorios.exception.ResourceNotFoundException;
import com.microservicio_laboratorios.microservicio_laboratorios.model.Laboratorio;
import com.microservicio_laboratorios.microservicio_laboratorios.repository.LaboratorioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    // Listar todos los laboratorios
    public List<Laboratorio> listarTodos() {
        log.info("Obteniendo todos los laboratorios");
        return laboratorioRepository.findAll();
    }

    // Buscar laboratorios por tipo de análisis
    public List<Laboratorio> buscarPorTipo(String tipoAnalisis) {
        log.info("Buscando laboratorios por tipo de análisis: {}", tipoAnalisis);
        List<Laboratorio> resultados = laboratorioRepository.findByTipoAnalisis(tipoAnalisis);

        if (resultados.isEmpty()) {
            log.warn("No se encontraron laboratorios con tipo de análisis: {}", tipoAnalisis);
            throw new ResourceNotFoundException("No se encontraron laboratorios con tipo de análisis: " + tipoAnalisis);
        }

        return resultados;
    }

    // Obtener laboratorio por ID con excepción
    public Laboratorio obtenerPorId(Long id) {
        log.info("Buscando laboratorio con ID {}", id);
        return laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con id: " + id));
    }

    // Crear nuevo laboratorio
    public Laboratorio crear(Laboratorio laboratorio) {
        log.info("Creando laboratorio con nombre: {}", laboratorio.getNombre());

        Long id = laboratorio.getId();
        if (id != null && laboratorioRepository.existsById(id)) {
            throw new BadRequestException("Ya existe un laboratorio con el ID especificado");
        }

        return laboratorioRepository.save(laboratorio);
    }

    // Actualizar laboratorio existente
    public Laboratorio actualizar(Long id, Laboratorio laboratorioActualizado) {
        log.info("Actualizando laboratorio con id {}", id);

        Laboratorio laboratorioExistente = laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede actualizar. Laboratorio no encontrado con id: " + id));

        laboratorioExistente.setNombre(laboratorioActualizado.getNombre());
        laboratorioExistente.setCapacidad(laboratorioActualizado.getCapacidad());
        laboratorioExistente.setEstado(laboratorioActualizado.getEstado());
        laboratorioExistente.setTipoAnalisis(laboratorioActualizado.getTipoAnalisis());

        return laboratorioRepository.save(laboratorioExistente);
    }

    // Eliminar laboratorio
    public void eliminar(Long id) {
        log.info("Eliminando laboratorio con id {}", id);

        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede eliminar. Laboratorio no encontrado con id: " + id));

        laboratorioRepository.delete(laboratorio);
        log.info("Laboratorio con id {} eliminado correctamente", id);
    }
}