package com.paolicias.catalogo.services;

import com.paolicias.catalogo.config.BusinessValidationException;
import com.paolicias.catalogo.config.ResourceNotFoundException;
import com.paolicias.catalogo.dto.CotizacionRequest;
import com.paolicias.catalogo.dto.CotizacionResponse;
import com.paolicias.catalogo.entities.TarifarioConfig;
import com.paolicias.catalogo.entities.TramoDescuento;
import com.paolicias.catalogo.repository.TarifarioConfigRepository;
import com.paolicias.catalogo.repository.TramoDescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private static final Long CONFIG_ID = 1L;

    private final TramoDescuentoRepository tramoDescuentoRepository;
    private final TarifarioConfigRepository tarifarioConfigRepository;

    @Override
    @Transactional(readOnly = true)
    public CotizacionResponse calcularCotizacion(CotizacionRequest request) {
        validarRequest(request);

        int cantidadComensales = request.cantidadComensales();
        int precioBaseUnitario = request.precioBaseUnitario();
        boolean incluirTorta = Boolean.TRUE.equals(request.incluirTortaPersonalizada());

        TramoDescuento tramo = tramoDescuentoRepository
                .findFirstByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(cantidadComensales, cantidadComensales)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró un tramo de descuento para " + cantidadComensales + " comensales"));

        TarifarioConfig config = tarifarioConfigRepository.findById(CONFIG_ID)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la configuración del tarifario"));

        int subtotalMenu = cantidadComensales * precioBaseUnitario;
        int porcentajeDescuentoTramo = tramo.getPorcentajeDescuento();
        int descuentoEscalonado = (subtotalMenu * porcentajeDescuentoTramo) / 100;
        int costoTortaPersonalizada = incluirTorta ? (cantidadComensales * config.getPrecioTortaPorPersona()) : 0;
        int montoTotalCotizado = (subtotalMenu - descuentoEscalonado) + costoTortaPersonalizada;
        int montoAbonoCincuentaPorCiento = (montoTotalCotizado * config.getPorcentajeAbono()) / 100;

        String dotacionEstimada = calcularDotacionEstimada(cantidadComensales, incluirTorta);

        return new CotizacionResponse(
                request.menuSeleccionado(),
                cantidadComensales,
                precioBaseUnitario,
                incluirTorta,
                tramo.getNombreTramo(),
                porcentajeDescuentoTramo,
                subtotalMenu,
                costoTortaPersonalizada,
                descuentoEscalonado,
                montoTotalCotizado,
                montoAbonoCincuentaPorCiento,
                dotacionEstimada
        );
    }

    private void validarRequest(CotizacionRequest request) {
        if (request == null) {
            throw new BusinessValidationException("La solicitud de cotización es obligatoria");
        }
        if (request.menuSeleccionado() == null || request.menuSeleccionado().isBlank()) {
            throw new BusinessValidationException("El menú seleccionado no puede ser nulo ni estar en blanco");
        }
        Integer comensales = request.cantidadComensales();
        if (comensales == null || comensales < 10 || comensales > 100 || comensales % 10 != 0) {
            throw new BusinessValidationException(
                    "La cantidad de comensales debe estar entre 10 y 100 inclusive y ser múltiplo de 10");
        }
        Integer precioBase = request.precioBaseUnitario();
        if (precioBase == null || precioBase < 5000) {
            throw new BusinessValidationException("El precio base unitario debe ser mayor o igual a 5000");
        }
    }

    private String calcularDotacionEstimada(int cantidadComensales, boolean incluirTorta) {
        int garzones = Math.max(1, cantidadComensales / 20);
        int ayudantesCocina = (cantidadComensales >= 50) ? 2 : 1;
        int reposteros = incluirTorta ? 1 : 0;
        return garzones + " garzón(es), " + ayudantesCocina + " ayudante(s) de cocina"
                + (reposteros > 0 ? ", " + reposteros + " repostero(a)" : "");
    }
}
