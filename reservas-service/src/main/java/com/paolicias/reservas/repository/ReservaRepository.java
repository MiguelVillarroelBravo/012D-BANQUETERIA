package com.paolicias.reservas.repository;

import com.paolicias.reservas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Listar las reservas de un cliente ordenadas de más reciente a más antigua
    List<Reserva> findByClienteEmailOrderByIdDesc(String clienteEmail);

    // Listar todas ordenadas cronológicamente por fecha del evento (para el Administrador)
    List<Reserva> findAllByOrderByFechaEventoAsc();

    // Filtrar por estado ordenadas cronológicamente
    List<Reserva> findByEstadoOrderByFechaEventoAsc(String estado);

    // Filtrar por fecha exacta
    List<Reserva> findByFechaEventoOrderByIdAsc(String fechaEvento);

    // Filtrar por estado y fecha a la vez
    List<Reserva> findByEstadoAndFechaEvento(String estado, String fechaEvento);
}