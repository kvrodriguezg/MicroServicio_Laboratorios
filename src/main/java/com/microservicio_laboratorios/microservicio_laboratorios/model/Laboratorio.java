package com.microservicio_laboratorios.microservicio_laboratorios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "laboratorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laboratorio {

    // Creación de campos de la entidad y sus validaciones

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del laboratorio es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Min(value = 1, message = "La capacidad mínima es 1")
    @Max(value = 1000, message = "La capacidad máxima es 1000")
    @Column(nullable = false)
    private int capacidad;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(ACTIVO|INACTIVO)$", message = "Estado debe ser ACTIVO o INACTIVO")
    @Column(nullable = false, length = 10)
    private String estado;

    @NotBlank(message = "Tipo de análisis es obligatorio")
    @Column(nullable = false, length = 50)
    private String tipoAnalisis;

    @Column(length = 500)
    private String descripcion;

    private String ubicacion;

    private String imagen;
}