package com.paolicias.catalogo.services;

import com.paolicias.catalogo.dto.TarifarioConfigResponse;
import com.paolicias.catalogo.dto.TarifarioConfigUpdateRequest;
import com.paolicias.catalogo.dto.TramoDescuentoResponse;
import com.paolicias.catalogo.dto.TramoDescuentoUpdateRequest;

import java.util.List;

public interface TarifarioService {

    List<TramoDescuentoResponse> listarTramos();

    TramoDescuentoResponse actualizarPorcentajeTramo(Long id, TramoDescuentoUpdateRequest request);

    TarifarioConfigResponse obtenerConfiguracion();

    TarifarioConfigResponse actualizarConfiguracion(TarifarioConfigUpdateRequest request);
}
