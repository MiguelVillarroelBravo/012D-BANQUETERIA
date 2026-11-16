package com.paolicias.catalogo.services;

import com.paolicias.catalogo.config.ResourceNotFoundException;
import com.paolicias.catalogo.dto.ServicioRequest;
import com.paolicias.catalogo.dto.ServicioResponse;
import com.paolicias.catalogo.entities.Servicio;
import com.paolicias.catalogo.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarServicios(Boolean soloActivos) {
        List<Servicio> servicios = Boolean.TRUE.equals(soloActivos)
                ? servicioRepository.findByActivoTrue()
                : servicioRepository.findAll();
        return servicios.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorId(Long id) {
        Servicio servicio = findServicioOrThrow(id);
        return toResponse(servicio);
    }

    @Override
    @Transactional
    public ServicioResponse crearServicio(ServicioRequest request) {
        Servicio servicio = new Servicio();
        servicio.setNombre(request.nombre());
        servicio.setDescripcion(request.descripcion());
        servicio.setPrecioUnitario(request.precioUnitario());
        servicio.setActivo(request.activo() != null ? request.activo() : true);
        return toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public ServicioResponse actualizarServicio(Long id, ServicioRequest request) {
        Servicio servicio = findServicioOrThrow(id);
        servicio.setNombre(request.nombre());
        servicio.setDescripcion(request.descripcion());
        servicio.setPrecioUnitario(request.precioUnitario());
        if (request.activo() != null) {
            servicio.setActivo(request.activo());
        }
        return toResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public void eliminarServicio(Long id) {
        Servicio servicio = findServicioOrThrow(id);
        servicioRepository.delete(servicio);
    }

    private Servicio findServicioOrThrow(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
    }

    private ServicioResponse toResponse(Servicio servicio) {
        return new ServicioResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecioUnitario(),
                servicio.getActivo()
        );
    }
}
