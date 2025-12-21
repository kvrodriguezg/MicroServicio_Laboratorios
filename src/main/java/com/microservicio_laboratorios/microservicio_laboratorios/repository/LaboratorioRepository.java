package com.microservicio_laboratorios.microservicio_laboratorios.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.microservicio_laboratorios.microservicio_laboratorios.model.Laboratorio;
import java.util.List;


public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    List<Laboratorio> findByTipoAnalisis(String tipoAnalisis);
}