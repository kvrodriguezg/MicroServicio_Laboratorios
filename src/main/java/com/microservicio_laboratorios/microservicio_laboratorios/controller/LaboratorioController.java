package com.microservicio_laboratorios.microservicio_laboratorios.controller;
import com.microservicio_laboratorios.microservicio_laboratorios.model.Laboratorio;
import com.microservicio_laboratorios.microservicio_laboratorios.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    //Obtener todos los laboratorios
    @GetMapping
    public ResponseEntity<List<Laboratorio>> listar() {
        return ResponseEntity.ok(laboratorioService.listarTodos());
    }

    //Obtener mediante id
    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(laboratorioService.obtenerPorId(id));
    }

    //Buscar mediante el tipo
    @GetMapping("/buscar")
    public ResponseEntity<List<Laboratorio>> buscarPorTipo(@RequestParam(required = false) String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return ResponseEntity.ok(laboratorioService.listarTodos());
        }
        return ResponseEntity.ok(laboratorioService.buscarPorTipo(tipo));
    }

    //Crear un laboratorio
    @PostMapping
    public ResponseEntity<Laboratorio> crear(@Valid @RequestBody Laboratorio laboratorio) {
        return ResponseEntity.status(201).body(laboratorioService.crear(laboratorio));
    }

    //Actualizar un laboratorio
    @PutMapping("/{id}")
    public ResponseEntity<Laboratorio> actualizar(@PathVariable Long id, @Valid @RequestBody Laboratorio laboratorio) {
        return ResponseEntity.ok(laboratorioService.actualizar(id, laboratorio));
    }

    //Eliminar laboratorio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        laboratorioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}