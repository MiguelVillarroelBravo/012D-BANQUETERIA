package com.paolicias.catalogo.services;

import com.paolicias.catalogo.config.ResourceNotFoundException;
import com.paolicias.catalogo.dto.TarifarioConfigResponse;
import com.paolicias.catalogo.dto.TarifarioConfigUpdateRequest;
import com.paolicias.catalogo.dto.TramoDescuentoResponse;
import com.paolicias.catalogo.dto.TramoDescuentoUpdateRequest;
import com.paolicias.catalogo.entities.TarifarioConfig;
import com.paolicias.catalogo.entities.TramoDescuento;
import com.paolicias.catalogo.repository.TarifarioConfigRepository;
import com.paolicias.catalogo.repository.TramoDescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifarioServiceImpl implements TarifarioService {

    private static final Long CONFIG_ID = 1L;

    private final TramoDescuentoRepository tramoDescuentoRepository;
    private final TarifarioConfigRepository tarifarioConfigRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TramoDescuentoResponse> listarTramos() {
        return tramoDescuentoRepository.findAllByOrderByMinPersonasAsc().stream()
                .map(this::toTramoResponse)
                .toList();
    }

    @Override
    @Transactional
    public TramoDescuentoResponse actualizarPorcentajeTramo(Long id, TramoDescuentoUpdateRequest request) {
        TramoDescuento tramo = tramoDescuentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo de descuento no encontrado con id: " + id));
        tramo.setPorcentajeDescuento(request.porcentajeDescuento());
        return toTramoResponse(tramoDescuentoRepository.save(tramo));
    }

    @Override
    @Transactional(readOnly = true)
    public TarifarioConfigResponse obtenerConfiguracion() {
        TarifarioConfig config = findConfigOrThrow();
        return toConfigResponse(config);
    }

    @Override
    @Transactional
    public TarifarioConfigResponse actualizarConfiguracion(TarifarioConfigUpdateRequest request) {
        TarifarioConfig config = findConfigOrThrow();
        config.setPrecioTortaPorPersona(request.precioTortaPorPersona());
        return toConfigResponse(tarifarioConfigRepository.save(config));
    }

    private TarifarioConfig findConfigOrThrow() {
        return tarifarioConfigRepository.findById(CONFIG_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Configuración de tarifario no encontrada"));
    }

    private TramoDescuentoResponse toTramoResponse(TramoDescuento tramo) {
        return new TramoDescuentoResponse(
                tramo.getId(),
                tramo.getNombreTramo(),
                tramo.getMinPersonas(),
                tramo.getMaxPersonas(),
                tramo.getPorcentajeDescuento()
        );
    }

    private TarifarioConfigResponse toConfigResponse(TarifarioConfig config) {
        return new TarifarioConfigResponse(
                config.getPrecioTortaPorPersona(),
                config.getPorcentajeAbono()
        );
    }
}
