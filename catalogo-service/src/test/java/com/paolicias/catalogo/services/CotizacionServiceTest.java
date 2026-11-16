package com.paolicias.catalogo.services;

import com.paolicias.catalogo.config.BusinessValidationException;
import com.paolicias.catalogo.dto.CotizacionRequest;
import com.paolicias.catalogo.dto.CotizacionResponse;
import com.paolicias.catalogo.entities.TarifarioConfig;
import com.paolicias.catalogo.entities.TramoDescuento;
import com.paolicias.catalogo.repository.TarifarioConfigRepository;
import com.paolicias.catalogo.repository.TramoDescuentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CotizacionServiceTest {

    @Mock
    private TramoDescuentoRepository tramoDescuentoRepository;

    @Mock
    private TarifarioConfigRepository tarifarioConfigRepository;

    @InjectMocks
    private CotizacionServiceImpl cotizacionService;

    private void mockConfigPorDefecto() {
        when(tarifarioConfigRepository.findById(1L))
                .thenReturn(Optional.of(new TarifarioConfig(1L, 1500, 50)));
    }

    private void mockTramo(int cantidad, String nombre, int min, int max, int porcentaje) {
        when(tramoDescuentoRepository
                .findFirstByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(cantidad, cantidad))
                .thenReturn(Optional.of(new TramoDescuento(1L, nombre, min, max, porcentaje)));
    }

    @ParameterizedTest(name = "Comensales={0} -> tramo={1} ({2}%)")
    @CsvSource({
            "10, 'Tramo Inicial (10 a 20 personas)', 10, 20, 0",
            "20, 'Tramo Inicial (10 a 20 personas)', 10, 20, 0",
            "30, 'Tramo Medio (30 a 50 personas)', 30, 50, 5",
            "50, 'Tramo Medio (30 a 50 personas)', 30, 50, 5",
            "60, 'Tramo Corporativo (60 a 80 personas)', 60, 80, 10",
            "80, 'Tramo Corporativo (60 a 80 personas)', 60, 80, 10",
            "90, 'Tramo Máximo (90 a 100 personas)', 90, 100, 15",
            "100, 'Tramo Máximo (90 a 100 personas)', 90, 100, 15"
    })
    @DisplayName("Debe aplicar correctamente el porcentaje de descuento en casos límite de cada tramo")
    void debeAplicarTramoDescuentoEnCasosLimite(
            int comensales,
            String nombreTramo,
            int min,
            int max,
            int porcentajeEsperado) {
        mockConfigPorDefecto();
        mockTramo(comensales, nombreTramo, min, max, porcentajeEsperado);

        CotizacionRequest request = new CotizacionRequest(
                "Catering Corporativo Premium",
                comensales,
                10000,
                false
        );

        CotizacionResponse response = cotizacionService.calcularCotizacion(request);

        assertEquals(nombreTramo, response.tramoAplicado());
        assertEquals(porcentajeEsperado, response.porcentajeDescuentoTramo());
        int subtotalEsperado = comensales * 10000;
        int descuentoEsperado = (subtotalEsperado * porcentajeEsperado) / 100;
        assertEquals(subtotalEsperado, response.subtotalMenu());
        assertEquals(descuentoEsperado, response.descuentoEscalonado());
    }

    @Test
    @DisplayName("Debe calcular montos exactos con torta personalizada incluida ($1.500 p/p) y abono del 50%")
    void debeCalcularMontosExactosConTortaPersonalizada() {
        mockConfigPorDefecto();
        mockTramo(40, "Tramo Medio (30 a 50 personas)", 30, 50, 5);

        CotizacionRequest request = new CotizacionRequest(
                "Catering Corporativo Premium",
                40,
                20000,
                true
        );

        CotizacionResponse response = cotizacionService.calcularCotizacion(request);

        // subtotal = 40 * 20000 = 800000
        // descuento 5% = 40000
        // torta = 40 * 1500 = 60000
        // total = (800000 - 40000) + 60000 = 820000
        // abono 50% = 410000
        assertEquals(800000, response.subtotalMenu());
        assertEquals(40000, response.descuentoEscalonado());
        assertEquals(60000, response.costoTortaPersonalizada());
        assertEquals(820000, response.montoTotalCotizado());
        assertEquals(410000, response.montoAbonoCincuentaPorCiento());
        assertEquals(response.montoTotalCotizado() / 2, response.montoAbonoCincuentaPorCiento());
    }

    @Test
    @DisplayName("Debe calcular montos exactos sin torta personalizada ($0) y abono del 50%")
    void debeCalcularMontosExactosSinTortaPersonalizada() {
        mockConfigPorDefecto();
        mockTramo(40, "Tramo Medio (30 a 50 personas)", 30, 50, 5);

        CotizacionRequest request = new CotizacionRequest(
                "Catering Corporativo Premium",
                40,
                20000,
                false
        );

        CotizacionResponse response = cotizacionService.calcularCotizacion(request);

        // subtotal = 800000, descuento 5% = 40000, torta = 0, total = 760000, abono = 380000
        assertEquals(800000, response.subtotalMenu());
        assertEquals(40000, response.descuentoEscalonado());
        assertEquals(0, response.costoTortaPersonalizada());
        assertEquals(760000, response.montoTotalCotizado());
        assertEquals(380000, response.montoAbonoCincuentaPorCiento());
        assertEquals(response.montoTotalCotizado() / 2, response.montoAbonoCincuentaPorCiento());
    }

    @Test
    @DisplayName("Debe calcular dotación de personal para 10 personas sin torta, 50 con torta y 100 con torta")
    void debeCalcularDotacionDePersonalCorrectamente() {
        mockConfigPorDefecto();

        mockTramo(10, "Tramo Inicial (10 a 20 personas)", 10, 20, 0);
        CotizacionResponse resp10 = cotizacionService.calcularCotizacion(
                new CotizacionRequest("Menú Base", 10, 10000, false));
        assertEquals("1 garzón(es), 1 ayudante(s) de cocina", resp10.dotacionEstimada());

        mockTramo(50, "Tramo Medio (30 a 50 personas)", 30, 50, 5);
        CotizacionResponse resp50 = cotizacionService.calcularCotizacion(
                new CotizacionRequest("Menú Base", 50, 10000, true));
        assertEquals("2 garzón(es), 2 ayudante(s) de cocina, 1 repostero(a)", resp50.dotacionEstimada());

        mockTramo(100, "Tramo Máximo (90 a 100 personas)", 90, 100, 15);
        CotizacionResponse resp100 = cotizacionService.calcularCotizacion(
                new CotizacionRequest("Menú Base", 100, 10000, true));
        assertEquals("5 garzón(es), 2 ayudante(s) de cocina, 1 repostero(a)", resp100.dotacionEstimada());
    }

    @ParameterizedTest(name = "Comensales inválidos={0}, precio={1}")
    @CsvSource({
            "5, 10000",
            "110, 10000",
            "25, 10000",
            "20, 4000"
    })
    @DisplayName("Debe lanzar BusinessValidationException cuando los parámetros de entrada son inválidos")
    void debeLanzarExcepcionConEntradasInvalidas(int comensales, int precioBase) {
        CotizacionRequest request = new CotizacionRequest(
                "Catering Corporativo Premium",
                comensales,
                precioBase,
                false
        );

        assertThrows(BusinessValidationException.class,
                () -> cotizacionService.calcularCotizacion(request));

        verifyNoInteractions(tramoDescuentoRepository, tarifarioConfigRepository);
    }

    @Test
    @DisplayName("Debe lanzar BusinessValidationException cuando menuSeleccionado está en blanco")
    void debeLanzarExcepcionCuandoMenuEstaEnBlanco() {
        CotizacionRequest request = new CotizacionRequest("   ", 20, 10000, false);

        assertThrows(BusinessValidationException.class,
                () -> cotizacionService.calcularCotizacion(request));

        verifyNoInteractions(tramoDescuentoRepository, tarifarioConfigRepository);
    }
}
