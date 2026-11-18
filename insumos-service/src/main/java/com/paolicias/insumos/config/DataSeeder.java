package com.paolicias.insumos.config;

import com.paolicias.insumos.entities.Insumo;
import com.paolicias.insumos.repository.InsumoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final InsumoRepository repository;

    public DataSeeder(InsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                new Insumo(null, "Harina", "Pastelería", "kg", 8.0, 0.05, 1200.0),
                new Insumo(null, "Azúcar", "Pastelería", "kg", 6.0, 0.04, 1100.0),
                new Insumo(null, "Mantequilla", "Lácteos", "kg", 3.0, 0.03, 6500.0),
                new Insumo(null, "Huevos", "Lácteos", "unidad", 60.0, 0.5, 250.0),
                new Insumo(null, "Crema de leche", "Lácteos", "lt", 4.0, 0.04, 2800.0),
                new Insumo(null, "Pollo", "Carnes", "kg", 10.0, 0.2, 4200.0),
                new Insumo(null, "Arroz", "Secos", "kg", 12.0, 0.1, 1300.0),
                new Insumo(null, "Vasos desechables", "Servicio", "unidad", 100.0, 1.0, 40.0)
        ));
    }
}