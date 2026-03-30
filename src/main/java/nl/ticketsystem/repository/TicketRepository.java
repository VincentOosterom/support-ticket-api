package nl.ticketsystem.repository;

import nl.ticketsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
