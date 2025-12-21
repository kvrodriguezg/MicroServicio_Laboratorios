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

    
    public List<Laboratorio> listarTodos() {
        log.info("Obteniendo todos los laboratorios");
        return laboratorioRepository.findAll();
    }

    
    public List<Laboratorio> buscarPorTipo(String tipoAnalisis) {
        log.info("Buscando laboratorios por tipo de análisis: {}", tipoAnalisis);
        List<Laboratorio> resultados = laboratorioRepository.findByTipoAnalisis(tipoAnalisis);

        if (resultados.isEmpty()) {
            log.warn("No se encontraron laboratorios con tipo de análisis: {}", tipoAnalisis);
            throw new ResourceNotFoundException("No se encontraron laboratorios con tipo de análisis: " + tipoAnalisis);
        }

        return resultados;
    }

    
    public Laboratorio obtenerPorId(Long id) {
        log.info("Buscando laboratorio con ID {}", id);
        return laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con id: " + id));
    }

    
    public Laboratorio crear(Laboratorio laboratorio) {
        log.info("Creando laboratorio con nombre: {}", laboratorio.getNombre());

        Long id = laboratorio.getId();
        if (id != null && laboratorioRepository.existsById(id)) {
            throw new BadRequestException("Ya existe un laboratorio con el ID especificado");
        }
        laboratorio.setId(null); 

        
        laboratorio.setImagen(obtenerImagenPorTipo(laboratorio.getTipoAnalisis()));

        return laboratorioRepository.save(laboratorio);
    }

    private String obtenerImagenPorTipo(String tipo) {
        if (tipo == null)
            return "assets/img/lab_clinico.png";
        switch (tipo.toLowerCase()) {
            case "clinico":
                return "assets/img/lab_clinico.png";
            case "investigacion":
                return "assets/img/lab_investigacion.png";
            case "educativo":
                return "assets/img/lab_educativo.png";
            case "industrial":
                return "assets/img/lab_industrial.png";
            default:
                return "assets/img/lab_clinico.png";
        }
    }

    
    public Laboratorio actualizar(Long id, Laboratorio laboratorioActualizado) {
        log.info("Actualizando laboratorio con id {}", id);

        Laboratorio laboratorioExistente = laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede actualizar. Laboratorio no encontrado con id: " + id));

        laboratorioExistente.setNombre(laboratorioActualizado.getNombre());
        laboratorioExistente.setCapacidad(laboratorioActualizado.getCapacidad());
        laboratorioExistente.setEstado(laboratorioActualizado.getEstado());
        laboratorioExistente.setTipoAnalisis(laboratorioActualizado.getTipoAnalisis());
        laboratorioExistente.setDescripcion(laboratorioActualizado.getDescripcion());
        laboratorioExistente.setUbicacion(laboratorioActualizado.getUbicacion());

        
        laboratorioExistente.setImagen(obtenerImagenPorTipo(laboratorioActualizado.getTipoAnalisis()));

        return laboratorioRepository.save(laboratorioExistente);
    }

    
    public void eliminar(Long id) {
        log.info("Eliminando laboratorio con id {}", id);

        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede eliminar. Laboratorio no encontrado con id: " + id));

        laboratorioRepository.delete(laboratorio);
        log.info("Laboratorio con id {} eliminado correctamente", id);
    }
}