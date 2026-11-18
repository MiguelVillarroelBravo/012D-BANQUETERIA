package com.paolicias.insumos.repository;

import com.paolicias.insumos.entities.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}