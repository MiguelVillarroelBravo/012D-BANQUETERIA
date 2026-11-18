package com.paolicias.insumos.services;

import com.paolicias.insumos.config.BusinessValidationException;
import com.paolicias.insumos.config.ResourceNotFoundException;
import com.paolicias.insumos.dto.InsumoRequest;
import com.paolicias.insumos.dto.PlanificacionItemResponse;
import com.paolicias.insumos.dto.PlanificacionResponse;
import com.paolicias.insumos.entities.Insumo;
import com.paolicias.insumos.repository.InsumoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InsumoServiceImpl implements InsumoService {

    private static final int MAX_PERSONAS = 10000;

    private final InsumoRepository insumoRepository;

    public InsumoServiceImpl(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    @Override
    public List<Insumo> listar() {
        return insumoRepository.findAll();
    }

    @Override
    public Insumo obtener(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + id));
    }

    @Override
    public Insumo crear(InsumoRequest r) {
        Insumo insumo = new Insumo(null, r.nombre().trim(), r.categoria().trim(), r.unidad().trim(),
                r.stock(), r.cantidadPorPersona(), r.costoUnitario());
        return insumoRepository.save(insumo);
    }

    @Override
    public Insumo actualizar(Long id, InsumoRequest r) {
        Insumo insumo = obtener(id);
        insumo.setNombre(r.nombre().trim());
        insumo.setCategoria(r.categoria().trim());
        insumo.setUnidad(r.unidad().trim());
        insumo.setStock(r.stock());
        insumo.setCantidadPorPersona(r.cantidadPorPersona());
        insumo.setCostoUnitario(r.costoUnitario());
        return insumoRepository.save(insumo);
    }

    @Override
    public void eliminar(Long id) {
        if (!insumoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Insumo no encontrado con id " + id);
        }
        insumoRepository.deleteById(id);
    }

    @Override
    public PlanificacionResponse planificar(int personas) {
        if (personas <= 0) {
            throw new BusinessValidationException("La cantidad de personas debe ser mayor a 0");
        }
        if (personas > MAX_PERSONAS) {
            throw new BusinessValidationException("La cantidad de personas no puede superar " + MAX_PERSONAS);
        }

        List<PlanificacionItemResponse> items = insumoRepository.findAll().stream().map(i -> {
            double requerido = BigDecimal.valueOf(i.getCantidadPorPersona())
                    .multiply(BigDecimal.valueOf(personas))
                    .setScale(0, RoundingMode.CEILING)
                    .doubleValue();
            double faltante = Math.max(0, requerido - i.getStock());
            long costo = Math.round(faltante * i.getCostoUnitario());
            return new PlanificacionItemResponse(i.getId(), i.getNombre(), i.getUnidad(),
                    requerido, i.getStock(), faltante, costo);
        }).toList();

        long total = items.stream().mapToLong(PlanificacionItemResponse::costoEstimado).sum();
        return new PlanificacionResponse(personas, items, total);
    }
}