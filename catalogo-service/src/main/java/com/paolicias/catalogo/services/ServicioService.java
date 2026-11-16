package com.paolicias.catalogo.services;

import com.paolicias.catalogo.dto.ServicioRequest;
import com.paolicias.catalogo.dto.ServicioResponse;

import java.util.List;

public interface ServicioService {

    List<ServicioResponse> listarServicios(Boolean soloActivos);

    ServicioResponse obtenerPorId(Long id);

    ServicioResponse crearServicio(ServicioRequest request);

    ServicioResponse actualizarServicio(Long id, ServicioRequest request);

    void eliminarServicio(Long id);
}
