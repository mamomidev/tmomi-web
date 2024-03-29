package org.hh99.tmomi.domain.ticket.repository;

import org.hh99.tmomi.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Ticket findByReservationId(String reservationId);

	List<Ticket> findAllByUserId(Long userId);
}
