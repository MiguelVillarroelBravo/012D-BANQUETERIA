package com.paolicias.catalogo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripción del servicio es obligatoria")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 5000, message = "El precio unitario debe ser al menos 5000")
    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;

    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;
}
