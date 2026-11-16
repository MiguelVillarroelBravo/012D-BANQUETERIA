package com.paolicias.catalogo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tramos_descuento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TramoDescuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_tramo", nullable = false)
    private String nombreTramo;

    @NotNull
    @Column(name = "min_personas", nullable = false)
    private Integer minPersonas;

    @NotNull
    @Column(name = "max_personas", nullable = false)
    private Integer maxPersonas;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "porcentaje_descuento", nullable = false)
    private Integer porcentajeDescuento;
}
