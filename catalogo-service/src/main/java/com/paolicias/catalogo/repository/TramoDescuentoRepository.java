package com.paolicias.catalogo.repository;

import com.paolicias.catalogo.entities.TramoDescuento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TramoDescuentoRepository extends JpaRepository<TramoDescuento, Long> {

    List<TramoDescuento> findAllByOrderByMinPersonasAsc();

    Optional<TramoDescuento> findFirstByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(
            Integer minPersonas,
            Integer maxPersonas
    );
}
