package com.paolicias.insumos.services;

import com.paolicias.insumos.config.BusinessValidationException;
import com.paolicias.insumos.config.ResourceNotFoundException;
import com.paolicias.insumos.dto.InsumoRequest;
import com.paolicias.insumos.dto.PlanificacionResponse;
import com.paolicias.insumos.entities.Insumo;
import com.paolicias.insumos.repository.InsumoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsumoServiceTest {

    @Mock
    private InsumoRepository insumoRepository;

    @InjectMocks
    private InsumoServiceImpl insumoService;

    private Insumo harina() {
        return new Insumo(1L, "Harina", "Pastelería", "kg", 10.0, 0.2, 1500.0);
    }

    @Test
    @DisplayName("Planificar 100 personas calcula requerido, faltante y costo")
    void planificarCalculaFaltanteYCosto() {
        when(insumoRepository.findAll()).thenReturn(List.of(harina()));

        PlanificacionResponse r = insumoService.planificar(100);

        assertEquals(20.0, r.items().get(0).requerido());
        assertEquals(10.0, r.items().get(0).faltante());
        assertEquals(15000L, r.items().get(0).costoEstimado());
        assertEquals(15000L, r.costoTotalEstimado());
    }

    @Test
    @DisplayName("Si el stock alcanza, el faltante y el costo son 0")
    void planificarSinFaltante() {
        when(insumoRepository.findAll()).thenReturn(List.of(harina()));

        PlanificacionResponse r = insumoService.planificar(10);

        assertEquals(0.0, r.items().get(0).faltante());
        assertEquals(0L, r.costoTotalEstimado());
    }

    @Test
    @DisplayName("Cantidad requerida se redondea hacia arriba")
    void planificarRedondeaHaciaArriba() {
        Insumo mantequilla = new Insumo(2L, "Mantequilla", "Lácteos", "kg", 0.0, 0.03, 6000.0);
        when(insumoRepository.findAll()).thenReturn(List.of(mantequilla));

        PlanificacionResponse r = insumoService.planificar(50);

        assertEquals(2.0, r.items().get(0).requerido());
        assertEquals(12000L, r.costoTotalEstimado());
    }

    @Test
    @DisplayName("Personas <= 0 lanza BusinessValidationException")
    void planificarPersonasInvalidas() {
        assertThrows(BusinessValidationException.class, () -> insumoService.planificar(0));
        assertThrows(BusinessValidationException.class, () -> insumoService.planificar(-5));
        verifyNoInteractions(insumoRepository);
    }

    @Test
    @DisplayName("Personas sobre el máximo lanza BusinessValidationException")
    void planificarPersonasSobreMaximo() {
        assertThrows(BusinessValidationException.class, () -> insumoService.planificar(10001));
    }

    @Test
    @DisplayName("Obtener insumo inexistente lanza ResourceNotFoundException")
    void obtenerInexistente() {
        when(insumoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> insumoService.obtener(99L));
    }

    @Test
    @DisplayName("Crear guarda el insumo con los datos recibidos")
    void crearGuardaInsumo() {
        when(insumoRepository.save(any(Insumo.class))).thenAnswer(inv -> inv.getArgument(0));
        InsumoRequest req = new InsumoRequest(" Sal ", "Secos", "kg", 5.0, 0.01, 800.0);

        Insumo creado = insumoService.crear(req);

        assertEquals("Sal", creado.getNombre());
        assertEquals(5.0, creado.getStock());
        verify(insumoRepository).save(any(Insumo.class));
    }

    @Test
    @DisplayName("Actualizar modifica los campos del insumo existente")
    void actualizarModificaCampos() {
        when(insumoRepository.findById(1L)).thenReturn(Optional.of(harina()));
        when(insumoRepository.save(any(Insumo.class))).thenAnswer(inv -> inv.getArgument(0));
        InsumoRequest req = new InsumoRequest("Harina integral", "Pastelería", "kg", 4.0, 0.3, 2000.0);

        Insumo r = insumoService.actualizar(1L, req);

        assertEquals("Harina integral", r.getNombre());
        assertEquals(4.0, r.getStock());
    }

    @Test
    @DisplayName("Eliminar inexistente lanza excepción y no borra")
    void eliminarInexistente() {
        when(insumoRepository.existsById(9L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> insumoService.eliminar(9L));
        verify(insumoRepository, never()).deleteById(any());
    }
}