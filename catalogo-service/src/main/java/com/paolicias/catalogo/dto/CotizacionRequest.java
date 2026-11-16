package com.paolicias.catalogo.dto;

public record CotizacionRequest(
        String menuSeleccionado,
        Integer cantidadComensales,
        Integer precioBaseUnitario,
        Boolean incluirTortaPersonalizada
) {
}
