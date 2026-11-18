package com.paolicias.insumos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "insumos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String unidad;

    @Column(nullable = false)
    private Double stock;

    @Column(name = "cantidad_por_persona", nullable = false)
    private Double cantidadPorPersona;

    @Column(name = "costo_unitario", nullable = false)
    private Double costoUnitario;
}