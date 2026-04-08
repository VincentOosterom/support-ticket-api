package nl.ticketsystem.repository;

import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
}
