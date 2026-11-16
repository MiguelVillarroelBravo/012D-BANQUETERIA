package com.paolicias.catalogo.dto;

public record CotizacionResponse(
        String menuSeleccionado,
        Integer cantidadComensales,
        Integer precioBaseUnitario,
        Boolean incluirTortaPersonalizada,
        String tramoAplicado,
        Integer porcentajeDescuentoTramo,
        Integer subtotalMenu,
        Integer costoTortaPersonalizada,
        Integer descuentoEscalonado,
        Integer montoTotalCotizado,
        Integer montoAbonoCincuentaPorCiento,
        String dotacionEstimada
) {
}
