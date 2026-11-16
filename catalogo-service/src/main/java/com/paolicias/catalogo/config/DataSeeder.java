package com.paolicias.catalogo.config;

import com.paolicias.catalogo.entities.Servicio;
import com.paolicias.catalogo.entities.TarifarioConfig;
import com.paolicias.catalogo.entities.TramoDescuento;
import com.paolicias.catalogo.repository.ServicioRepository;
import com.paolicias.catalogo.repository.TarifarioConfigRepository;
import com.paolicias.catalogo.repository.TramoDescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ServicioRepository servicioRepository;
    private final TramoDescuentoRepository tramoDescuentoRepository;
    private final TarifarioConfigRepository tarifarioConfigRepository;

    @Override
    public void run(String... args) {
        seedServicios();
        seedTramosDescuento();
        seedTarifarioConfig();
    }

    private void seedServicios() {
        if (servicioRepository.count() == 0) {
            servicioRepository.saveAll(List.of(
                    new Servicio(null, "Catering Corporativo Premium",
                            "Servicio completo para seminarios y reuniones de empresa. Incluye garzones y mantelería.",
                            85000, true),
                    new Servicio(null, "Pack Pastelería Fina (50 unidades)",
                            "Surtido de minipasteles finos artesanales para eventos y celebraciones.",
                            45000, true),
                    new Servicio(null, "Torta Personalizada Fondant",
                            "Bizcocho a elección con diseño y modelado en fondant personalizado.",
                            35000, true),
                    new Servicio(null, "Banquete Completo (Hasta 100 pers)",
                            "Servicio integral para eventos grandes con menú completo y logística de vajilla.",
                            250000, true)
            ));
        }
    }

    private void seedTramosDescuento() {
        if (tramoDescuentoRepository.count() == 0) {
            tramoDescuentoRepository.saveAll(List.of(
                    new TramoDescuento(null, "Tramo Inicial (10 a 20 personas)", 10, 20, 0),
                    new TramoDescuento(null, "Tramo Medio (30 a 50 personas)", 30, 50, 5),
                    new TramoDescuento(null, "Tramo Corporativo (60 a 80 personas)", 60, 80, 10),
                    new TramoDescuento(null, "Tramo Máximo (90 a 100 personas)", 90, 100, 15)
            ));
        }
    }

    private void seedTarifarioConfig() {
        if (tarifarioConfigRepository.count() == 0) {
            tarifarioConfigRepository.save(new TarifarioConfig(1L, 1500, 50));
        }
    }
}
