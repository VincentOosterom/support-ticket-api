package nl.ticketsystem.repository;

import nl.ticketsystem.model.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
}
