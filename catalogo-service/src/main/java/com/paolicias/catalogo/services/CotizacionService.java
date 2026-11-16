package com.paolicias.catalogo.services;

import com.paolicias.catalogo.dto.CotizacionRequest;
import com.paolicias.catalogo.dto.CotizacionResponse;

public interface CotizacionService {

    CotizacionResponse calcularCotizacion(CotizacionRequest request);
}
