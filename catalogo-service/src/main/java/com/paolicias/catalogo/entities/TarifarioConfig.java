package com.paolicias.catalogo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarifario_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifarioConfig {

    @Id
    private Long id = 1L;

    @NotNull
    @Min(0)
    @Column(name = "precio_torta_por_persona", nullable = false)
    private Integer precioTortaPorPersona = 1500;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "porcentaje_abono", nullable = false)
    private Integer porcentajeAbono = 50;
}
